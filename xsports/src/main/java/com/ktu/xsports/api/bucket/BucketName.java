package com.ktu.xsports.api.bucket;

public enum BucketName {
    IMAGE("xsports-images");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
