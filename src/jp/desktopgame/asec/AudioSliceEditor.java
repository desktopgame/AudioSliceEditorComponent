/*
 * AudioSliceEditorComponent
 *
 * Copyright (c) 2020 desktopgame
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package jp.desktopgame.asec;

import javax.swing.JComponent;

/**
 *
 * @author desktopgame
 */
public class AudioSliceEditor extends JComponent {

    private static final String uiClassID = "AudioSliceEditorUI";
    private float peakStartPosition;
    private float peakEndPosition;
    private int markerSize;

    public AudioSliceEditor() {
        this.peakStartPosition = 0.0f;
        this.peakEndPosition = 1.0f;
        this.markerSize = 10;
        updateUI();
    }

    public void setUI(AudioSliceEditorUI ui) {
        super.setUI(ui);
    }

    public AudioSliceEditorUI getUI() {
        return (AudioSliceEditorUI) ui;
    }

    @Override
    public void updateUI() {
        setUI(new BasicAudioSliceEditorUI());
    }

    @Override
    public String getUIClassID() {
        return uiClassID;
    }

    public void setPeakStartPosition(float peakStartPosition) {
        peakStartPosition = Math.max(0, peakStartPosition);
        float mw = (float) getMarkerSize() / (float) getWidth();
        float ff = (mw / 1.0f);
        if (peakStartPosition + ff >= this.peakEndPosition) {
            peakStartPosition = this.peakEndPosition - 0.01f - ff;
        }
        float a = this.peakStartPosition;
        this.peakStartPosition = peakStartPosition;
        firePropertyChange("peakStartPosition", a, this.peakStartPosition);
    }

    public float getPeakStartPosition() {
        return peakStartPosition;
    }

    public void setPeakEndPosition(float peakEndPosition) {
        peakEndPosition = Math.min(1.0f, peakEndPosition);
        float mw = (float) getMarkerSize() / (float) getWidth();
        float ff = (mw / 1.0f);
        if (peakEndPosition - ff <= this.peakStartPosition) {
            peakEndPosition = this.peakStartPosition + 0.01f + ff;
        }
        float a = this.peakEndPosition;
        this.peakEndPosition = peakEndPosition;
        firePropertyChange("peakEndPosition", a, this.peakEndPosition);
    }

    public float getPeakEndPosition() {
        return peakEndPosition;
    }

    public void setMarkerSize(int markerSize) {
        int a = this.markerSize;
        this.markerSize = markerSize;
        firePropertyChange("markerSize", a, this.markerSize);
    }

    public int getMarkerSize() {
        return markerSize;
    }
}
