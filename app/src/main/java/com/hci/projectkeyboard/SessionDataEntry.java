package com.hci.projectkeyboard;

public class SessionDataEntry {
    String keyboard;

    int phrase_no;
    String original_phrase, transcribed_phrase;
    long time_taken;
    double error_rate;

    long start_time, end_time;
    int msd;

    int id;

    public SessionDataEntry() {
    }

    public SessionDataEntry(String arg_keyboard, int arg_phrase_no, String arg_original_phrase,
                            String arg_transcribed_phrase, long arg_time_taken, double arg_error_rate,
                            long arg_start_time, long arg_end_time, int arg_msd, int arg_id) {
        this.keyboard = arg_keyboard;
        this.phrase_no = arg_phrase_no;
        this.original_phrase = arg_original_phrase;
        this.transcribed_phrase = arg_transcribed_phrase;
        this.time_taken = arg_time_taken;
        this.error_rate = arg_error_rate;
        this.start_time = arg_start_time;
        this.end_time = arg_end_time;
        this.msd = arg_msd;
        this.id = arg_id;
    }

    public SessionDataEntry(String arg_keyboard, int arg_phrase_no, String arg_original_phrase,
                            String arg_transcribed_phrase, long arg_time_taken, double arg_error_rate,
                            long arg_start_time, long arg_end_time, int arg_msd) {
        this.keyboard = arg_keyboard;
        this.phrase_no = arg_phrase_no;
        this.original_phrase = arg_original_phrase;
        this.transcribed_phrase = arg_transcribed_phrase;
        this.time_taken = arg_time_taken;
        this.error_rate = arg_error_rate;
        this.start_time = arg_start_time;
        this.end_time = arg_end_time;
        this.msd = arg_msd;
    }

    public int getID() {
        return this.id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setSessionDataEntry(String arg_keyboard, int arg_phrase_no, String arg_original_phrase,
                                    String arg_transcribed_phrase, long arg_time_taken, double arg_error_rate,
                                    long arg_start_time, long arg_end_time, int arg_msd) {
        this.keyboard = arg_keyboard;
        this.phrase_no = arg_phrase_no;
        this.original_phrase = arg_original_phrase;
        this.transcribed_phrase = arg_transcribed_phrase;
        this.time_taken = arg_time_taken;
        this.error_rate = arg_error_rate;
        this.start_time = arg_start_time;
        this.end_time = arg_end_time;
        this.msd = arg_msd;
    }
}
