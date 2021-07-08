package util;

import io.netty.util.AttributeKey;

import java.io.RandomAccessFile;

public class ChannelAttrUtil {

    public static final AttributeKey<RandomAccessFile> outStream = AttributeKey.valueOf("outStream");
    public static final AttributeKey<Long> fileSize = AttributeKey.valueOf("fileSize");
    public static final AttributeKey<Boolean> newFile = AttributeKey.valueOf("newFile");
}
