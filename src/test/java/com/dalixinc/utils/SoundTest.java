package com.dalixinc.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream; // For potential future use with AudioSystem mocking
import javax.sound.sampled.LineUnavailableException; // For exception testing
import javax.sound.sampled.UnsupportedAudioFileException; // For exception testing
import java.io.IOException; // For exception testing
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SoundTest {

    @Mock
    private Clip mockClip;

    private Sound sound;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sound = new Sound();
        sound.setClip(mockClip); // Inject the mock clip
    }

    @Test
    void constructor_populatesSoundURLs() {
        assertNotNull(sound.soundURLs, "soundURLs array should be initialized.");
        assertEquals(30, sound.soundURLs.length, "soundURLs array should have a length of 30.");

        // Check a few known URLs (first, middle-ish, last defined)
        assertNotNull(sound.soundURLs[0], "soundURLs[0] (AtlantisTune1.wav) should not be null.");
        assertTrue(sound.soundURLs[0].toString().endsWith("/sound/AtlantisTune1.wav"));

        assertNotNull(sound.soundURLs[5], "soundURLs[5] (authentication.wav) should not be null.");
        assertTrue(sound.soundURLs[5].toString().endsWith("/sound/authentication.wav"));
        
        assertNotNull(sound.soundURLs[10], "soundURLs[10] (pacman_death.wav) should not be null.");
        assertTrue(sound.soundURLs[10].toString().endsWith("/sound/pacman_death.wav"));

        // Check an undefined URL slot
        assertNull(sound.soundURLs[11], "soundURLs[11] (undefined) should be null.");
        assertNull(sound.soundURLs[29], "soundURLs[29] (undefined) should be null.");
    }

    @Test
    void setFile_withValidIndex_attemptsToLoadAndOpenClip() {
        // This test is limited because AudioSystem static methods are hard to mock without PowerMockito.
        // We are testing the behavior *around* those calls.
        // The actual clip loading will happen, but our mockClip won't be the one loaded by AudioSystem here.
        // However, after setFile runs, our injected mockClip is still the one used by play/loop/stop.
        
        // To truly test if AudioSystem.getClip() and clip.open() were called on the clip created by AudioSystem,
        // we would need a more complex setup.
        // For this test, we mainly ensure it doesn't crash for a valid index and that subsequent play/loop/stop
        // calls would go to our *injected* mockClip.

        // Call setFile with a valid index (where soundURLs[0] is expected to be non-null)
        // This will internally create a real Clip and attempt to open soundURLs[0].
        // This part might fail if the resource is not found or audio system is unavailable in test env.
        try {
            sound.setFile(0); 
        } catch (Exception e) {
            // In some CI environments, audio system might not be available.
            // We don't want the test to fail due to env issues for this specific part.
            System.err.println("Note: Exception during sound.setFile(0) in test, possibly due to audio system unavailability in test environment: " + e.getMessage());
        }
        
        // After sound.setFile(0) is called, sound.clip is replaced by a real clip.
        // For testing play/loop/stop against our mock, we must re-inject it.
        sound.setClip(mockClip); 

        // Now, verify that methods call on the mockClip
        sound.play();
        verify(mockClip).start();
    }

    @Test
    void setFile_withInvalidIndex_throwsArrayIndexOutOfBounds() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            sound.setFile(30); // Index out of bounds
        });
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            sound.setFile(-1); // Index out of bounds
        });
    }
    
    @Test
    void setFile_withNullURL_catchesException() {
        // soundURLs[11] is null by default after constructor
        // This should cause an exception within the try block of setFile, which is caught.
        // No standard Java exception is guaranteed for getAudioInputStream(null),
        // but it's likely an IOException or NullPointerException.
        // The current implementation catches generic Exception and prints stack trace.
        // This test verifies it doesn't re-throw an unhandled exception.
        assertDoesNotThrow(() -> {
            sound.setFile(11); 
        });
        // We can't easily verify the printStackTrace with standard Mockito.
        // The important part is that the application doesn't crash.
    }


    @Test
    void play_callsClipStart() {
        sound.play();
        verify(mockClip).start();
    }

    @Test
    void loop_callsClipLoopContinuously() {
        sound.loop();
        verify(mockClip).loop(Clip.LOOP_CONTINUOUSLY);
    }

    @Test
    void stop_callsClipStop() {
        sound.stop();
        verify(mockClip).stop();
    }
    
    // No checkVolume method in Sound.java to test.
}
