package com.example.doandidong.model;

public class NotiResponse {
    private long multicast_id;
    private int success,failure,canonical_ids;

    public NotiResponse(int canonical_ids, int failure, long multicast_id, int success) {
        this.canonical_ids = canonical_ids;
        this.failure = failure;
        this.multicast_id = multicast_id;
        this.success = success;
    }

    public int getCanonical_ids() {
        return canonical_ids;
    }

    public void setCanonical_ids(int canonical_ids) {
        this.canonical_ids = canonical_ids;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public long getMulticast_id() {
        return multicast_id;
    }

    public void setMulticast_id(long multicast_id) {
        this.multicast_id = multicast_id;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
