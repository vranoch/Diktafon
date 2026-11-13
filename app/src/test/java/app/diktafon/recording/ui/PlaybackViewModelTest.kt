package app.diktafon.recording.ui

import app.diktafon.recording.domain.AudioPlayer
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

private class FakeAudioPlayer : AudioPlayer {
    var playCalls = 0
    var pauseCalls = 0
    var stopCalls = 0
    override fun play(file: File) {
        playCalls++
    }
    override fun pause() {
        pauseCalls++
    }
    override fun stop() {
        stopCalls++
    }
    override fun isPlaying(): Boolean = playCalls > pauseCalls
}

class PlaybackViewModelTest {

    @Test
    fun `prvni prepnuti spusti prehravani`() {
        val player = FakeAudioPlayer()
        val vm = PlaybackViewModel(player)

        vm.togglePlayback(File("/tmp/a.m4a"))

        assertTrue(vm.isPlaying.value)
        assertEqualsOne(player.playCalls)
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

    private fun assertEqualsOne(value: Int) {
        assertTrue(value == 1)
    }
}
