package com.realmax.smarthomeversion2.activity.bean;

import java.util.List;

public class AcAndTvAndMusicBean {

    /**
     * ac_S : [{"acPower":0,"mode":0,"windSpeed":5,"temperature":19,"currentTemperature":20},{"acPower":0,"mode":0,"windSpeed":5,"temperature":19,"currentTemperature":20}]
     * tv_S : {"tvPower":0,"tvShow":9,"volume":14}
     * music_S : {"musicPower":0,"musicSelect":9,"volume":14}
     */

    private TvSBean tv_S;
    private MusicSBean music_S;
    private List<AcSBean> ac_S;

    public AcAndTvAndMusicBean() {
    }

    public AcAndTvAndMusicBean(TvSBean tv_S) {
        this.tv_S = tv_S;
    }

    public AcAndTvAndMusicBean(MusicSBean music_S) {
        this.music_S = music_S;
    }

    public AcAndTvAndMusicBean(List<AcSBean> ac_S) {
        this.ac_S = ac_S;
    }

    public TvSBean getTv_S() {
        return tv_S;
    }

    public void setTv_S(TvSBean tv_S) {
        this.tv_S = tv_S;
    }

    public MusicSBean getMusic_S() {
        return music_S;
    }

    public void setMusic_S(MusicSBean music_S) {
        this.music_S = music_S;
    }

    public List<AcSBean> getAc_S() {
        return ac_S;
    }

    public void setAc_S(List<AcSBean> ac_S) {
        this.ac_S = ac_S;
    }

    public static class TvSBean {
        /**
         * tvPower : 0
         * tvShow : 9
         * volume : 14
         */

        private int tvPower = 0;
        private int tvShow = 0;
        private int volume = 1;

        public int getTvPower() {
            return tvPower;
        }

        public void setTvPower(int tvPower) {
            this.tvPower = tvPower;
        }

        public int getTvShow() {
            return tvShow;
        }

        public void setTvShow(int tvShow) {
            this.tvShow = tvShow;
        }

        public int getVolume() {
            return volume;
        }

        public void setVolume(int volume) {
            this.volume = volume;
        }

        @Override
        public String toString() {
            return "TvSBean{" +
                    "tvPower=" + tvPower +
                    ", tvShow=" + tvShow +
                    ", volume=" + volume +
                    '}';
        }
    }

    public static class MusicSBean {
        /**
         * musicPower : 0
         * musicSelect : 9
         * volume : 14
         */

        private int musicPower;
        private int musicSelect;
        private int volume;

        public int getMusicPower() {
            return musicPower;
        }

        public void setMusicPower(int musicPower) {
            this.musicPower = musicPower;
        }

        public int getMusicSelect() {
            return musicSelect;
        }

        public void setMusicSelect(int musicSelect) {
            this.musicSelect = musicSelect;
        }

        public int getVolume() {
            return volume;
        }

        public void setVolume(int volume) {
            this.volume = volume;
        }

        @Override
        public String toString() {
            return "MusicSBean{" +
                    "musicPower=" + musicPower +
                    ", musicSelect=" + musicSelect +
                    ", volume=" + volume +
                    '}';
        }
    }

    public static class AcSBean {
        /**
         * acPower : 0
         * mode : 0
         * windSpeed : 5
         * temperature : 19
         * currentTemperature : 20
         */

        private int acPower;
        private int mode;
        private int windSpeed;
        private int temperature;
        private int currentTemperature;

        public int getAcPower() {
            return acPower;
        }

        public void setAcPower(int acPower) {
            this.acPower = acPower;
        }

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        public int getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(int windSpeed) {
            this.windSpeed = windSpeed;
        }

        public int getTemperature() {
            return temperature;
        }

        public void setTemperature(int temperature) {
            this.temperature = temperature;
        }

        public int getCurrentTemperature() {
            return currentTemperature;
        }

        public void setCurrentTemperature(int currentTemperature) {
            this.currentTemperature = currentTemperature;
        }

        @Override
        public String toString() {
            return "AcSBean{" +
                    "acPower=" + acPower +
                    ", mode=" + mode +
                    ", windSpeed=" + windSpeed +
                    ", temperature=" + temperature +
                    ", currentTemperature=" + currentTemperature +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AcAndTvAndMusicBean{" +
                "tv_S=" + tv_S +
                ", music_S=" + music_S +
                ", ac_S=" + ac_S +
                '}';
    }
}
