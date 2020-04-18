package com.realmax.smarthomeversion2.bean;

public class DoorBean {

    /**
     * door1_C : {"pass_c":1234,"lock_c":1,"door_C":0}
     * door1_S : {"pass":1234,"lock_s":1,"door_s":1}
     * door2_C : {"pass_c":1234,"lock_c":1,"door_C":0}
     * door2_S : {"pass":1234,"lock_s":1,"door_s":1}
     * door3_C : {"pass_c":1234,"lock_c":1,"door_C":0}
     * door3_S : {"pass":1234,"lock_s":1,"door_s":1}
     * door4_C : {"pass_c":1234,"lock_c":1,"door_C":0}
     * door4_S : {"pass":1234,"lock_s":1,"door_s":1}
     * door5_C : {"pass_c":1234,"lock_c":1,"door_C":0}
     * door5_S : {"pass":1234,"lock_s":1,"door_s":1}
     */

    private Door1CBean door1_C;
    private Door1SBean door1_S;
    private Door2CBean door2_C;
    private Door2SBean door2_S;
    private Door3CBean door3_C;
    private Door3SBean door3_S;
    private Door4CBean door4_C;
    private Door4SBean door4_S;
    private Door5CBean door5_C;
    private Door5SBean door5_S;

    public Door1CBean getDoor1_C() {
        return door1_C;
    }

    public void setDoor1_C(Door1CBean door1_C) {
        this.door1_C = door1_C;
    }

    public Door1SBean getDoor1_S() {
        return door1_S;
    }

    public void setDoor1_S(Door1SBean door1_S) {
        this.door1_S = door1_S;
    }

    public Door2CBean getDoor2_C() {
        return door2_C;
    }

    public void setDoor2_C(Door2CBean door2_C) {
        this.door2_C = door2_C;
    }

    public Door2SBean getDoor2_S() {
        return door2_S;
    }

    public void setDoor2_S(Door2SBean door2_S) {
        this.door2_S = door2_S;
    }

    public Door3CBean getDoor3_C() {
        return door3_C;
    }

    public void setDoor3_C(Door3CBean door3_C) {
        this.door3_C = door3_C;
    }

    public Door3SBean getDoor3_S() {
        return door3_S;
    }

    public void setDoor3_S(Door3SBean door3_S) {
        this.door3_S = door3_S;
    }

    public Door4CBean getDoor4_C() {
        return door4_C;
    }

    public void setDoor4_C(Door4CBean door4_C) {
        this.door4_C = door4_C;
    }

    public Door4SBean getDoor4_S() {
        return door4_S;
    }

    public void setDoor4_S(Door4SBean door4_S) {
        this.door4_S = door4_S;
    }

    public Door5CBean getDoor5_C() {
        return door5_C;
    }

    public void setDoor5_C(Door5CBean door5_C) {
        this.door5_C = door5_C;
    }

    public Door5SBean getDoor5_S() {
        return door5_S;
    }

    public void setDoor5_S(Door5SBean door5_S) {
        this.door5_S = door5_S;
    }

    public static class Door1CBean {
        /**
         * pass_c : 1234
         * lock_c : 1
         * door_C : 0
         */

        private int pass_c = -1;
        private int lock_c = -1;
        private int door_C = -1;

        public int getPass_c() {
            return pass_c;
        }

        public void setPass_c(int pass_c) {
            this.pass_c = pass_c;
        }

        public int getLock_c() {
            return lock_c;
        }

        public void setLock_c(int lock_c) {
            this.lock_c = lock_c;
        }

        public int getDoor_C() {
            return door_C;
        }

        public void setDoor_C(int door_C) {
            this.door_C = door_C;
        }

        @Override
        public String toString() {
            return "Door1CBean{" +
                    "pass_c=" + pass_c +
                    ", lock_c=" + lock_c +
                    ", door_C=" + door_C +
                    '}';
        }
    }

    public static class Door1SBean {
        /**
         * pass : 1234
         * lock_s : 1
         * door_s : 1
         */

        private int pass = -1;
        private int lock_s = -1;
        private int door_s = -1;

        public int getPass() {
            return pass;
        }

        public void setPass(int pass) {
            this.pass = pass;
        }

        public int getLock_s() {
            return lock_s;
        }

        public void setLock_s(int lock_s) {
            this.lock_s = lock_s;
        }

        public int getDoor_s() {
            return door_s;
        }

        public void setDoor_s(int door_s) {
            this.door_s = door_s;
        }

        @Override
        public String toString() {
            return "Door1SBean{" +
                    "pass=" + pass +
                    ", lock_s=" + lock_s +
                    ", door_s=" + door_s +
                    '}';
        }
    }

    public static class Door2CBean {
        /**
         * pass_c : 1234
         * lock_c : 1
         * door_C : 0
         */

        private int pass_c = -1;
        private int lock_c = -1;
        private int door_C = -1;

        public int getPass_c() {
            return pass_c;
        }

        public void setPass_c(int pass_c) {
            this.pass_c = pass_c;
        }

        public int getLock_c() {
            return lock_c;
        }

        public void setLock_c(int lock_c) {
            this.lock_c = lock_c;
        }

        public int getDoor_C() {
            return door_C;
        }

        public void setDoor_C(int door_C) {
            this.door_C = door_C;
        }

        @Override
        public String toString() {
            return "Door2CBean{" +
                    "pass_c=" + pass_c +
                    ", lock_c=" + lock_c +
                    ", door_C=" + door_C +
                    '}';
        }
    }

    public static class Door2SBean {
        /**
         * pass : 1234
         * lock_s : 1
         * door_s : 1
         */

        private int pass = -1;
        private int lock_s = -1;
        private int door_s = -1;

        public int getPass() {
            return pass;
        }

        public void setPass(int pass) {
            this.pass = pass;
        }

        public int getLock_s() {
            return lock_s;
        }

        public void setLock_s(int lock_s) {
            this.lock_s = lock_s;
        }

        public int getDoor_s() {
            return door_s;
        }

        public void setDoor_s(int door_s) {
            this.door_s = door_s;
        }

        @Override
        public String toString() {
            return "Door2SBean{" +
                    "pass=" + pass +
                    ", lock_s=" + lock_s +
                    ", door_s=" + door_s +
                    '}';
        }
    }

    public static class Door3CBean {
        /**
         * pass_c : 1234
         * lock_c : 1
         * door_C : 0
         */

        private int pass_c = -1;
        private int lock_c = -1;
        private int door_C = -1;

        public int getPass_c() {
            return pass_c;
        }

        public void setPass_c(int pass_c) {
            this.pass_c = pass_c;
        }

        public int getLock_c() {
            return lock_c;
        }

        public void setLock_c(int lock_c) {
            this.lock_c = lock_c;
        }

        public int getDoor_C() {
            return door_C;
        }

        public void setDoor_C(int door_C) {
            this.door_C = door_C;
        }

        @Override
        public String toString() {
            return "Door3CBean{" +
                    "pass_c=" + pass_c +
                    ", lock_c=" + lock_c +
                    ", door_C=" + door_C +
                    '}';
        }
    }

    public static class Door3SBean {
        /**
         * pass : 1234
         * lock_s : 1
         * door_s : 1
         */

        private int pass = -1;
        private int lock_s = -1;
        private int door_s = -1;

        public int getPass() {
            return pass;
        }

        public void setPass(int pass) {
            this.pass = pass;
        }

        public int getLock_s() {
            return lock_s;
        }

        public void setLock_s(int lock_s) {
            this.lock_s = lock_s;
        }

        public int getDoor_s() {
            return door_s;
        }

        public void setDoor_s(int door_s) {
            this.door_s = door_s;
        }

        @Override
        public String toString() {
            return "Door3SBean{" +
                    "pass=" + pass +
                    ", lock_s=" + lock_s +
                    ", door_s=" + door_s +
                    '}';
        }
    }

    public static class Door4CBean {
        /**
         * pass_c : 1234
         * lock_c : 1
         * door_C : 0
         */

        private int pass_c = -1;
        private int lock_c = -1;
        private int door_C = -1;

        public int getPass_c() {
            return pass_c;
        }

        public void setPass_c(int pass_c) {
            this.pass_c = pass_c;
        }

        public int getLock_c() {
            return lock_c;
        }

        public void setLock_c(int lock_c) {
            this.lock_c = lock_c;
        }

        public int getDoor_C() {
            return door_C;
        }

        public void setDoor_C(int door_C) {
            this.door_C = door_C;
        }

        @Override
        public String toString() {
            return "Door4CBean{" +
                    "pass_c=" + pass_c +
                    ", lock_c=" + lock_c +
                    ", door_C=" + door_C +
                    '}';
        }
    }

    public static class Door4SBean {
        /**
         * pass : 1234
         * lock_s : 1
         * door_s : 1
         */

        private int pass = -1;
        private int lock_s = -1;
        private int door_s = -1;

        public int getPass() {
            return pass;
        }

        public void setPass(int pass) {
            this.pass = pass;
        }

        public int getLock_s() {
            return lock_s;
        }

        public void setLock_s(int lock_s) {
            this.lock_s = lock_s;
        }

        public int getDoor_s() {
            return door_s;
        }

        public void setDoor_s(int door_s) {
            this.door_s = door_s;
        }

        @Override
        public String toString() {
            return "Door4SBean{" +
                    "pass=" + pass +
                    ", lock_s=" + lock_s +
                    ", door_s=" + door_s +
                    '}';
        }
    }

    public static class Door5CBean {
        /**
         * pass_c : 1234
         * lock_c : 1
         * door_C : 0
         */

        private int pass_c = -1;
        private int lock_c = -1;
        private int door_C = -1;

        public int getPass_c() {
            return pass_c;
        }

        public void setPass_c(int pass_c) {
            this.pass_c = pass_c;
        }

        public int getLock_c() {
            return lock_c;
        }

        public void setLock_c(int lock_c) {
            this.lock_c = lock_c;
        }

        public int getDoor_C() {
            return door_C;
        }

        public void setDoor_C(int door_C) {
            this.door_C = door_C;
        }

        @Override
        public String toString() {
            return "Door5CBean{" +
                    "pass_c=" + pass_c +
                    ", lock_c=" + lock_c +
                    ", door_C=" + door_C +
                    '}';
        }
    }

    public static class Door5SBean {
        /**
         * pass : 1234
         * lock_s : 1
         * door_s : 1
         */

        private int pass = -1;
        private int lock_s = -1;
        private int door_s = -1;

        public int getPass() {
            return pass;
        }

        public void setPass(int pass) {
            this.pass = pass;
        }

        public int getLock_s() {
            return lock_s;
        }

        public void setLock_s(int lock_s) {
            this.lock_s = lock_s;
        }

        public int getDoor_s() {
            return door_s;
        }

        public void setDoor_s(int door_s) {
            this.door_s = door_s;
        }

        @Override
        public String toString() {
            return "Door5SBean{" +
                    "pass=" + pass +
                    ", lock_s=" + lock_s +
                    ", door_s=" + door_s +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DoorBean{" +
                "door1_C=" + door1_C +
                ", door1_S=" + door1_S +
                ", door2_C=" + door2_C +
                ", door2_S=" + door2_S +
                ", door3_C=" + door3_C +
                ", door3_S=" + door3_S +
                ", door4_C=" + door4_C +
                ", door4_S=" + door4_S +
                ", door5_C=" + door5_C +
                ", door5_S=" + door5_S +
                '}';
    }
}
