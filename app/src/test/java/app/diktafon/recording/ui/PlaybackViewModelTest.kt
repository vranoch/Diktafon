package app.diktafon.recording.ui

import app.diktafon.recording.domain.AudioPlayer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

private class FakeAudioPlayer : AudioPlayer {
    var playCalls = 0
    var pauseCalls = 0
    var stopCalls = 0
    var position = 0
    var duration = 10_000
    var seekCalls = mutableListOf<Int>()
    var naturallyFinished = false

    override fun play(file: File) {
        playCalls++
        naturallyFinished = false
    }
    override fun pause() {
        pauseCalls++
    }
    override fun stop() {
        stopCalls++
    }
    override fun isPlaying(): Boolean = playCalls > pauseCalls && !naturallyFinished
    override fun currentPositionMs(): Int = position
    override fun durationMs(): Int = duration
    override fun seekTo(positionMs: Int) {
        position = positionMs
        seekCalls += positionMs
    }
}

class PlaybackViewModelTest {

    @Test
    fun `prvni prepnuti spusti prehravani a nastavi delku`() {
        val player = FakeAudioPlayer()
        val vm = PlaybackViewModel(player)

        vm.togglePlayback(File("/tmp/a.m4a"))

        assertTrue(vm.isPlaying.value)
        assertEqualsOne(player.playCalls)
        assertEquals(10_000, vm.durationMs.value)
    }

    @Test
    fun `druhe prepnuti pozastavi prehravani`() {
        val player = FakeAudioPlayer()
        val vm = PlaybackViewModel(player)
        vm.togglePlayback(File("/tmp/a.m4a"))

        vm.togglePlayback(File("/tmp/a.m4a"))

        assertFalse(vm.isPlaying.value)
        assertEqualsOne(player.pauseCalls)
    }

    @Test
    fun `tick aktualizuje pozici behem prehravani`() {
        val player = FakeAudioPlayer()
        val vm = PlaybackViewModel(player)
        vm.togglePlayback(File("/tmp/a.m4a"))
        player.position = 3_000

        vm.tick()

        assertEquals(3_000, vm.positionMs.value)
    }

    @Test
    fun `prirozene dohrani nastavi isPlaying na false a vynuluje pozici`() {
        val player = FakeAudioPlayer()
        val vm = PlaybackViewModel(player)
        vm.togglePlayback(File("/tmp/a.m4a"))
        player.naturallyFinished = true

        vm.tick()

        assertFalse(vm.isPlaying.value)
        assertEquals(0, vm.positionMs.value)
    }

    @Test
    fun `stop vynuluje pozici a zastavi prehravani`() {
        val player = FakeAudioPlayer()
        val vm = PlaybackViewModel(player)
        vm.togglePlayback(File("/tmp/a.m4a"))
        player.position = 5_000

        vm.stop()

        assertFalse(vm.isPlaying.value)
        assertEquals(0, vm.positionMs.value)
        assertEqualsOne(player.stopCalls)
    }

    @Test
    fun `seekTo preda pozici prehravaci a aktualizuje stav`() {
        val player = FakeAudioPlayer()
        val vm = PlaybackViewModel(player)

        vm.seekTo(2_500)

        assertEquals(listOf(2_500), player.seekCalls)
        assertEquals(2_500, vm.positionMs.value)
    }

    private fun assertEqualsOne(value: Int) {
        assertTrue(value == 1)
    }
}
