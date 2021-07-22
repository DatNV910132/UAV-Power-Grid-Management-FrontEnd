package com.dji.sdk.sample.internal.audiohandler;

/**
 * The type Media recorder options.
 */
public class MediaRecorderOptions {

    private Builder mBuilder;
    private MediaRecorderOptions(Builder builder) {
        mBuilder = builder;
    }

    /**
     * Gets audio sampling rate.
     *
     * @return the audio sampling rate
     */
    public int getAudioSamplingRate() {
        return mBuilder.mAudioSamplingRate;
    }

    /**
     * Gets audio encoding bit rate.
     *
     * @return the audio encoding bit rate
     */
    public int getAudioEncodingBitRate() {
        return mBuilder.mAudioEncodingBitRate;
    }

    /**
     * Gets audio channels.
     *
     * @return the audio channels
     */
    public int getAudioChannels() {
        return mBuilder.mAudioChannels;
    }

    /**
     * The type Builder.
     */
    public static class Builder {
        /**
         * The M audio sampling rate.
         */
        int mAudioSamplingRate;
        /**
         * The M audio encoding bit rate.
         */
        int mAudioEncodingBitRate;
        /**
         * The M audio channels.
         */
        int mAudioChannels;

        /**
         * Instantiates a new Builder.
         */
        public Builder() {
            mAudioSamplingRate = 44100;
            mAudioEncodingBitRate = 16000;
            mAudioChannels = 1;
        }

        /**
         * Audio sampling rate builder.
         *
         * @param rate the rate
         * @return the builder
         */
        public Builder audioSamplingRate(int rate) {
            mAudioSamplingRate = rate;
            return this;
        }

        /**
         * Audio encoding bit rate builder.
         *
         * @param audioEncodingBitRate the audio encoding bit rate
         * @return the builder
         */
        public Builder audioEncodingBitRate(int audioEncodingBitRate) {
            mAudioEncodingBitRate = audioEncodingBitRate;
            return this;
        }

        /**
         * Audio channels builder.
         *
         * @param audioChannels the audio channels
         * @return the builder
         */
        public Builder audioChannels(int audioChannels) {
            mAudioChannels = audioChannels;
            return this;
        }

        /**
         * Build media recorder options.
         *
         * @return the media recorder options
         */
        public MediaRecorderOptions build() {
            return new MediaRecorderOptions(this);
        }
    }
}
