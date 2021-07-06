package com.hhu.util;

import io.netty.util.AttributeKey;

import java.io.FileOutputStream;

public class ChannelAttrUtil {

    public static final AttributeKey<FileOutputStream> outStream = AttributeKey.valueOf("outStream");
    public static final AttributeKey<Long> fileSize = AttributeKey.valueOf("fileSize");

}
