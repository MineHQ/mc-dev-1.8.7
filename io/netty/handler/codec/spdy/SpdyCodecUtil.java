package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;

final class SpdyCodecUtil {
   static final int SPDY_SESSION_STREAM_ID = 0;
   static final int SPDY_HEADER_TYPE_OFFSET = 2;
   static final int SPDY_HEADER_FLAGS_OFFSET = 4;
   static final int SPDY_HEADER_LENGTH_OFFSET = 5;
   static final int SPDY_HEADER_SIZE = 8;
   static final int SPDY_MAX_LENGTH = 16777215;
   static final byte SPDY_DATA_FLAG_FIN = 1;
   static final int SPDY_DATA_FRAME = 0;
   static final int SPDY_SYN_STREAM_FRAME = 1;
   static final int SPDY_SYN_REPLY_FRAME = 2;
   static final int SPDY_RST_STREAM_FRAME = 3;
   static final int SPDY_SETTINGS_FRAME = 4;
   static final int SPDY_PUSH_PROMISE_FRAME = 5;
   static final int SPDY_PING_FRAME = 6;
   static final int SPDY_GOAWAY_FRAME = 7;
   static final int SPDY_HEADERS_FRAME = 8;
   static final int SPDY_WINDOW_UPDATE_FRAME = 9;
   static final byte SPDY_FLAG_FIN = 1;
   static final byte SPDY_FLAG_UNIDIRECTIONAL = 2;
   static final byte SPDY_SETTINGS_CLEAR = 1;
   static final byte SPDY_SETTINGS_PERSIST_VALUE = 1;
   static final byte SPDY_SETTINGS_PERSISTED = 2;
   static final int SPDY_SETTINGS_MAX_ID = 16777215;
   static final int SPDY_MAX_NV_LENGTH = 65535;
   static final byte[] SPDY_DICT = new byte[]{(byte)0, (byte)0, (byte)0, (byte)7, (byte)111, (byte)112, (byte)116, (byte)105, (byte)111, (byte)110, (byte)115, (byte)0, (byte)0, (byte)0, (byte)4, (byte)104, (byte)101, (byte)97, (byte)100, (byte)0, (byte)0, (byte)0, (byte)4, (byte)112, (byte)111, (byte)115, (byte)116, (byte)0, (byte)0, (byte)0, (byte)3, (byte)112, (byte)117, (byte)116, (byte)0, (byte)0, (byte)0, (byte)6, (byte)100, (byte)101, (byte)108, (byte)101, (byte)116, (byte)101, (byte)0, (byte)0, (byte)0, (byte)5, (byte)116, (byte)114, (byte)97, (byte)99, (byte)101, (byte)0, (byte)0, (byte)0, (byte)6, (byte)97, (byte)99, (byte)99, (byte)101, (byte)112, (byte)116, (byte)0, (byte)0, (byte)0, (byte)14, (byte)97, (byte)99, (byte)99, (byte)101, (byte)112, (byte)116, (byte)45, (byte)99, (byte)104, (byte)97, (byte)114, (byte)115, (byte)101, (byte)116, (byte)0, (byte)0, (byte)0, (byte)15, (byte)97, (byte)99, (byte)99, (byte)101, (byte)112, (byte)116, (byte)45, (byte)101, (byte)110, (byte)99, (byte)111, (byte)100, (byte)105, (byte)110, (byte)103, (byte)0, (byte)0, (byte)0, (byte)15, (byte)97, (byte)99, (byte)99, (byte)101, (byte)112, (byte)116, (byte)45, (byte)108, (byte)97, (byte)110, (byte)103, (byte)117, (byte)97, (byte)103, (byte)101, (byte)0, (byte)0, (byte)0, (byte)13, (byte)97, (byte)99, (byte)99, (byte)101, (byte)112, (byte)116, (byte)45, (byte)114, (byte)97, (byte)110, (byte)103, (byte)101, (byte)115, (byte)0, (byte)0, (byte)0, (byte)3, (byte)97, (byte)103, (byte)101, (byte)0, (byte)0, (byte)0, (byte)5, (byte)97, (byte)108, (byte)108, (byte)111, (byte)119, (byte)0, (byte)0, (byte)0, (byte)13, (byte)97, (byte)117, (byte)116, (byte)104, (byte)111, (byte)114, (byte)105, (byte)122, (byte)97, (byte)116, (byte)105, (byte)111, (byte)110, (byte)0, (byte)0, (byte)0, (byte)13, (byte)99, (byte)97, (byte)99, (byte)104, (byte)101, (byte)45, (byte)99, (byte)111, (byte)110, (byte)116, (byte)114, (byte)111, (byte)108, (byte)0, (byte)0, (byte)0, (byte)10, (byte)99, (byte)111, (byte)110, (byte)110, (byte)101, (byte)99, (byte)116, (byte)105, (byte)111, (byte)110, (byte)0, (byte)0, (byte)0, (byte)12, (byte)99, (byte)111, (byte)110, (byte)116, (byte)101, (byte)110, (byte)116, (byte)45, (byte)98, (byte)97, (byte)115, (byte)101, (byte)0, (byte)0, (byte)0, (byte)16, (byte)99, (byte)111, (byte)110, (byte)116, (byte)101, (byte)110, (byte)116, (byte)45, (byte)101, (byte)110, (byte)99, (byte)111, (byte)100, (byte)105, (byte)110, (byte)103, (byte)0, (byte)0, (byte)0, (byte)16, (byte)99, (byte)111, (byte)110, (byte)116, (byte)101, (byte)110, (byte)116, (byte)45, (byte)108, (byte)97, (byte)110, (byte)103, (byte)117, (byte)97, (byte)103, (byte)101, (byte)0, (byte)0, (byte)0, (byte)14, (byte)99, (byte)111, (byte)110, (byte)116, (byte)101, (byte)110, (byte)116, (byte)45, (byte)108, (byte)101, (byte)110, (byte)103, (byte)116, (byte)104, (byte)0, (byte)0, (byte)0, (byte)16, (byte)99, (byte)111, (byte)110, (byte)116, (byte)101, (byte)110, (byte)116, (byte)45, (byte)108, (byte)111, (byte)99, (byte)97, (byte)116, (byte)105, (byte)111, (byte)110, (byte)0, (byte)0, (byte)0, (byte)11, (byte)99, (byte)111, (byte)110, (byte)116, (byte)101, (byte)110, (byte)116, (byte)45, (byte)109, (byte)100, (byte)53, (byte)0, (byte)0, (byte)0, (byte)13, (byte)99, (byte)111, (byte)110, (byte)116, (byte)101, (byte)110, (byte)116, (byte)45, (byte)114, (byte)97, (byte)110, (byte)103, (byte)101, (byte)0, (byte)0, (byte)0, (byte)12, (byte)99, (byte)111, (byte)110, (byte)116, (byte)101, (byte)110, (byte)116, (byte)45, (byte)116, (byte)121, (byte)112, (byte)101, (byte)0, (byte)0, (byte)0, (byte)4, (byte)100, (byte)97, (byte)116, (byte)101, (byte)0, (byte)0, (byte)0, (byte)4, (byte)101, (byte)116, (byte)97, (byte)103, (byte)0, (byte)0, (byte)0, (byte)6, (byte)101, (byte)120, (byte)112, (byte)101, (byte)99, (byte)116, (byte)0, (byte)0, (byte)0, (byte)7, (byte)101, (byte)120, (byte)112, (byte)105, (byte)114, (byte)101, (byte)115, (byte)0, (byte)0, (byte)0, (byte)4, (byte)102, (byte)114, (byte)111, (byte)109, (byte)0, (byte)0, (byte)0, (byte)4, (byte)104, (byte)111, (byte)115, (byte)116, (byte)0, (byte)0, (byte)0, (byte)8, (byte)105, (byte)102, (byte)45, (byte)109, (byte)97, (byte)116, (byte)99, (byte)104, (byte)0, (byte)0, (byte)0, (byte)17, (byte)105, (byte)102, (byte)45, (byte)109, (byte)111, (byte)100, (byte)105, (byte)102, (byte)105, (byte)101, (byte)100, (byte)45, (byte)115, (byte)105, (byte)110, (byte)99, (byte)101, (byte)0, (byte)0, (byte)0, (byte)13, (byte)105, (byte)102, (byte)45, (byte)110, (byte)111, (byte)110, (byte)101, (byte)45, (byte)109, (byte)97, (byte)116, (byte)99, (byte)104, (byte)0, (byte)0, (byte)0, (byte)8, (byte)105, (byte)102, (byte)45, (byte)114, (byte)97, (byte)110, (byte)103, (byte)101, (byte)0, (byte)0, (byte)0, (byte)19, (byte)105, (byte)102, (byte)45, (byte)117, (byte)110, (byte)109, (byte)111, (byte)100, (byte)105, (byte)102, (byte)105, (byte)101, (byte)100, (byte)45, (byte)115, (byte)105, (byte)110, (byte)99, (byte)101, (byte)0, (byte)0, (byte)0, (byte)13, (byte)108, (byte)97, (byte)115, (byte)116, (byte)45, (byte)109, (byte)111, (byte)100, (byte)105, (byte)102, (byte)105, (byte)101, (byte)100, (byte)0, (byte)0, (byte)0, (byte)8, (byte)108, (byte)111, (byte)99, (byte)97, (byte)116, (byte)105, (byte)111, (byte)110, (byte)0, (byte)0, (byte)0, (byte)12, (byte)109, (byte)97, (byte)120, (byte)45, (byte)102, (byte)111, (byte)114, (byte)119, (byte)97, (byte)114, (byte)100, (byte)115, (byte)0, (byte)0, (byte)0, (byte)6, (byte)112, (byte)114, (byte)97, (byte)103, (byte)109, (byte)97, (byte)0, (byte)0, (byte)0, (byte)18, (byte)112, (byte)114, (byte)111, (byte)120, (byte)121, (byte)45, (byte)97, (byte)117, (byte)116, (byte)104, (byte)101, (byte)110, (byte)116, (byte)105, (byte)99, (byte)97, (byte)116, (byte)101, (byte)0, (byte)0, (byte)0, (byte)19, (byte)112, (byte)114, (byte)111, (byte)120, (byte)121, (byte)45, (byte)97, (byte)117, (byte)116, (byte)104, (byte)111, (byte)114, (byte)105, (byte)122, (byte)97, (byte)116, (byte)105, (byte)111, (byte)110, (byte)0, (byte)0, (byte)0, (byte)5, (byte)114, (byte)97, (byte)110, (byte)103, (byte)101, (byte)0, (byte)0, (byte)0, (byte)7, (byte)114, (byte)101, (byte)102, (byte)101, (byte)114, (byte)101, (byte)114, (byte)0, (byte)0, (byte)0, (byte)11, (byte)114, (byte)101, (byte)116, (byte)114, (byte)121, (byte)45, (byte)97, (byte)102, (byte)116, (byte)101, (byte)114, (byte)0, (byte)0, (byte)0, (byte)6, (byte)115, (byte)101, (byte)114, (byte)118, (byte)101, (byte)114, (byte)0, (byte)0, (byte)0, (byte)2, (byte)116, (byte)101, (byte)0, (byte)0, (byte)0, (byte)7, (byte)116, (byte)114, (byte)97, (byte)105, (byte)108, (byte)101, (byte)114, (byte)0, (byte)0, (byte)0, (byte)17, (byte)116, (byte)114, (byte)97, (byte)110, (byte)115, (byte)102, (byte)101, (byte)114, (byte)45, (byte)101, (byte)110, (byte)99, (byte)111, (byte)100, (byte)105, (byte)110, (byte)103, (byte)0, (byte)0, (byte)0, (byte)7, (byte)117, (byte)112, (byte)103, (byte)114, (byte)97, (byte)100, (byte)101, (byte)0, (byte)0, (byte)0, (byte)10, (byte)117, (byte)115, (byte)101, (byte)114, (byte)45, (byte)97, (byte)103, (byte)101, (byte)110, (byte)116, (byte)0, (byte)0, (byte)0, (byte)4, (byte)118, (byte)97, (byte)114, (byte)121, (byte)0, (byte)0, (byte)0, (byte)3, (byte)118, (byte)105, (byte)97, (byte)0, (byte)0, (byte)0, (byte)7, (byte)119, (byte)97, (byte)114, (byte)110, (byte)105, (byte)110, (byte)103, (byte)0, (byte)0, (byte)0, (byte)16, (byte)119, (byte)119, (byte)119, (byte)45, (byte)97, (byte)117, (byte)116, (byte)104, (byte)101, (byte)110, (byte)116, (byte)105, (byte)99, (byte)97, (byte)116, (byte)101, (byte)0, (byte)0, (byte)0, (byte)6, (byte)109, (byte)101, (byte)116, (byte)104, (byte)111, (byte)100, (byte)0, (byte)0, (byte)0, (byte)3, (byte)103, (byte)101, (byte)116, (byte)0, (byte)0, (byte)0, (byte)6, (byte)115, (byte)116, (byte)97, (byte)116, (byte)117, (byte)115, (byte)0, (byte)0, (byte)0, (byte)6, (byte)50, (byte)48, (byte)48, (byte)32, (byte)79, (byte)75, (byte)0, (byte)0, (byte)0, (byte)7, (byte)118, (byte)101, (byte)114, (byte)115, (byte)105, (byte)111, (byte)110, (byte)0, (byte)0, (byte)0, (byte)8, (byte)72, (byte)84, (byte)84, (byte)80, (byte)47, (byte)49, (byte)46, (byte)49, (byte)0, (byte)0, (byte)0, (byte)3, (byte)117, (byte)114, (byte)108, (byte)0, (byte)0, (byte)0, (byte)6, (byte)112, (byte)117, (byte)98, (byte)108, (byte)105, (byte)99, (byte)0, (byte)0, (byte)0, (byte)10, (byte)115, (byte)101, (byte)116, (byte)45, (byte)99, (byte)111, (byte)111, (byte)107, (byte)105, (byte)101, (byte)0, (byte)0, (byte)0, (byte)10, (byte)107, (byte)101, (byte)101, (byte)112, (byte)45, (byte)97, (byte)108, (byte)105, (byte)118, (byte)101, (byte)0, (byte)0, (byte)0, (byte)6, (byte)111, (byte)114, (byte)105, (byte)103, (byte)105, (byte)110, (byte)49, (byte)48, (byte)48, (byte)49, (byte)48, (byte)49, (byte)50, (byte)48, (byte)49, (byte)50, (byte)48, (byte)50, (byte)50, (byte)48, (byte)53, (byte)50, (byte)48, (byte)54, (byte)51, (byte)48, (byte)48, (byte)51, (byte)48, (byte)50, (byte)51, (byte)48, (byte)51, (byte)51, (byte)48, (byte)52, (byte)51, (byte)48, (byte)53, (byte)51, (byte)48, (byte)54, (byte)51, (byte)48, (byte)55, (byte)52, (byte)48, (byte)50, (byte)52, (byte)48, (byte)53, (byte)52, (byte)48, (byte)54, (byte)52, (byte)48, (byte)55, (byte)52, (byte)48, (byte)56, (byte)52, (byte)48, (byte)57, (byte)52, (byte)49, (byte)48, (byte)52, (byte)49, (byte)49, (byte)52, (byte)49, (byte)50, (byte)52, (byte)49, (byte)51, (byte)52, (byte)49, (byte)52, (byte)52, (byte)49, (byte)53, (byte)52, (byte)49, (byte)54, (byte)52, (byte)49, (byte)55, (byte)53, (byte)48, (byte)50, (byte)53, (byte)48, (byte)52, (byte)53, (byte)48, (byte)53, (byte)50, (byte)48, (byte)51, (byte)32, (byte)78, (byte)111, (byte)110, (byte)45, (byte)65, (byte)117, (byte)116, (byte)104, (byte)111, (byte)114, (byte)105, (byte)116, (byte)97, (byte)116, (byte)105, (byte)118, (byte)101, (byte)32, (byte)73, (byte)110, (byte)102, (byte)111, (byte)114, (byte)109, (byte)97, (byte)116, (byte)105, (byte)111, (byte)110, (byte)50, (byte)48, (byte)52, (byte)32, (byte)78, (byte)111, (byte)32, (byte)67, (byte)111, (byte)110, (byte)116, (byte)101, (byte)110, (byte)116, (byte)51, (byte)48, (byte)49, (byte)32, (byte)77, (byte)111, (byte)118, (byte)101, (byte)100, (byte)32, (byte)80, (byte)101, (byte)114, (byte)109, (byte)97, (byte)110, (byte)101, (byte)110, (byte)116, (byte)108, (byte)121, (byte)52, (byte)48, (byte)48, (byte)32, (byte)66, (byte)97, (byte)100, (byte)32, (byte)82, (byte)101, (byte)113, (byte)117, (byte)101, (byte)115, (byte)116, (byte)52, (byte)48, (byte)49, (byte)32, (byte)85, (byte)110, (byte)97, (byte)117, (byte)116, (byte)104, (byte)111, (byte)114, (byte)105, (byte)122, (byte)101, (byte)100, (byte)52, (byte)48, (byte)51, (byte)32, (byte)70, (byte)111, (byte)114, (byte)98, (byte)105, (byte)100, (byte)100, (byte)101, (byte)110, (byte)52, (byte)48, (byte)52, (byte)32, (byte)78, (byte)111, (byte)116, (byte)32, (byte)70, (byte)111, (byte)117, (byte)110, (byte)100, (byte)53, (byte)48, (byte)48, (byte)32, (byte)73, (byte)110, (byte)116, (byte)101, (byte)114, (byte)110, (byte)97, (byte)108, (byte)32, (byte)83, (byte)101, (byte)114, (byte)118, (byte)101, (byte)114, (byte)32, (byte)69, (byte)114, (byte)114, (byte)111, (byte)114, (byte)53, (byte)48, (byte)49, (byte)32, (byte)78, (byte)111, (byte)116, (byte)32, (byte)73, (byte)109, (byte)112, (byte)108, (byte)101, (byte)109, (byte)101, (byte)110, (byte)116, (byte)101, (byte)100, (byte)53, (byte)48, (byte)51, (byte)32, (byte)83, (byte)101, (byte)114, (byte)118, (byte)105, (byte)99, (byte)101, (byte)32, (byte)85, (byte)110, (byte)97, (byte)118, (byte)97, (byte)105, (byte)108, (byte)97, (byte)98, (byte)108, (byte)101, (byte)74, (byte)97, (byte)110, (byte)32, (byte)70, (byte)101, (byte)98, (byte)32, (byte)77, (byte)97, (byte)114, (byte)32, (byte)65, (byte)112, (byte)114, (byte)32, (byte)77, (byte)97, (byte)121, (byte)32, (byte)74, (byte)117, (byte)110, (byte)32, (byte)74, (byte)117, (byte)108, (byte)32, (byte)65, (byte)117, (byte)103, (byte)32, (byte)83, (byte)101, (byte)112, (byte)116, (byte)32, (byte)79, (byte)99, (byte)116, (byte)32, (byte)78, (byte)111, (byte)118, (byte)32, (byte)68, (byte)101, (byte)99, (byte)32, (byte)48, (byte)48, (byte)58, (byte)48, (byte)48, (byte)58, (byte)48, (byte)48, (byte)32, (byte)77, (byte)111, (byte)110, (byte)44, (byte)32, (byte)84, (byte)117, (byte)101, (byte)44, (byte)32, (byte)87, (byte)101, (byte)100, (byte)44, (byte)32, (byte)84, (byte)104, (byte)117, (byte)44, (byte)32, (byte)70, (byte)114, (byte)105, (byte)44, (byte)32, (byte)83, (byte)97, (byte)116, (byte)44, (byte)32, (byte)83, (byte)117, (byte)110, (byte)44, (byte)32, (byte)71, (byte)77, (byte)84, (byte)99, (byte)104, (byte)117, (byte)110, (byte)107, (byte)101, (byte)100, (byte)44, (byte)116, (byte)101, (byte)120, (byte)116, (byte)47, (byte)104, (byte)116, (byte)109, (byte)108, (byte)44, (byte)105, (byte)109, (byte)97, (byte)103, (byte)101, (byte)47, (byte)112, (byte)110, (byte)103, (byte)44, (byte)105, (byte)109, (byte)97, (byte)103, (byte)101, (byte)47, (byte)106, (byte)112, (byte)103, (byte)44, (byte)105, (byte)109, (byte)97, (byte)103, (byte)101, (byte)47, (byte)103, (byte)105, (byte)102, (byte)44, (byte)97, (byte)112, (byte)112, (byte)108, (byte)105, (byte)99, (byte)97, (byte)116, (byte)105, (byte)111, (byte)110, (byte)47, (byte)120, (byte)109, (byte)108, (byte)44, (byte)97, (byte)112, (byte)112, (byte)108, (byte)105, (byte)99, (byte)97, (byte)116, (byte)105, (byte)111, (byte)110, (byte)47, (byte)120, (byte)104, (byte)116, (byte)109, (byte)108, (byte)43, (byte)120, (byte)109, (byte)108, (byte)44, (byte)116, (byte)101, (byte)120, (byte)116, (byte)47, (byte)112, (byte)108, (byte)97, (byte)105, (byte)110, (byte)44, (byte)116, (byte)101, (byte)120, (byte)116, (byte)47, (byte)106, (byte)97, (byte)118, (byte)97, (byte)115, (byte)99, (byte)114, (byte)105, (byte)112, (byte)116, (byte)44, (byte)112, (byte)117, (byte)98, (byte)108, (byte)105, (byte)99, (byte)112, (byte)114, (byte)105, (byte)118, (byte)97, (byte)116, (byte)101, (byte)109, (byte)97, (byte)120, (byte)45, (byte)97, (byte)103, (byte)101, (byte)61, (byte)103, (byte)122, (byte)105, (byte)112, (byte)44, (byte)100, (byte)101, (byte)102, (byte)108, (byte)97, (byte)116, (byte)101, (byte)44, (byte)115, (byte)100, (byte)99, (byte)104, (byte)99, (byte)104, (byte)97, (byte)114, (byte)115, (byte)101, (byte)116, (byte)61, (byte)117, (byte)116, (byte)102, (byte)45, (byte)56, (byte)99, (byte)104, (byte)97, (byte)114, (byte)115, (byte)101, (byte)116, (byte)61, (byte)105, (byte)115, (byte)111, (byte)45, (byte)56, (byte)56, (byte)53, (byte)57, (byte)45, (byte)49, (byte)44, (byte)117, (byte)116, (byte)102, (byte)45, (byte)44, (byte)42, (byte)44, (byte)101, (byte)110, (byte)113, (byte)61, (byte)48, (byte)46};

   private SpdyCodecUtil() {
   }

   static int getUnsignedShort(ByteBuf var0, int var1) {
      return (var0.getByte(var1) & 255) << 8 | var0.getByte(var1 + 1) & 255;
   }

   static int getUnsignedMedium(ByteBuf var0, int var1) {
      return (var0.getByte(var1) & 255) << 16 | (var0.getByte(var1 + 1) & 255) << 8 | var0.getByte(var1 + 2) & 255;
   }

   static int getUnsignedInt(ByteBuf var0, int var1) {
      return (var0.getByte(var1) & 127) << 24 | (var0.getByte(var1 + 1) & 255) << 16 | (var0.getByte(var1 + 2) & 255) << 8 | var0.getByte(var1 + 3) & 255;
   }

   static int getSignedInt(ByteBuf var0, int var1) {
      return (var0.getByte(var1) & 255) << 24 | (var0.getByte(var1 + 1) & 255) << 16 | (var0.getByte(var1 + 2) & 255) << 8 | var0.getByte(var1 + 3) & 255;
   }

   static boolean isServerId(int var0) {
      return var0 % 2 == 0;
   }

   static void validateHeaderName(String var0) {
      if(var0 == null) {
         throw new NullPointerException("name");
      } else if(var0.isEmpty()) {
         throw new IllegalArgumentException("name cannot be length zero");
      } else if(var0.length() > '\uffff') {
         throw new IllegalArgumentException("name exceeds allowable length: " + var0);
      } else {
         for(int var1 = 0; var1 < var0.length(); ++var1) {
            char var2 = var0.charAt(var1);
            if(var2 == 0) {
               throw new IllegalArgumentException("name contains null character: " + var0);
            }

            if(var2 > 127) {
               throw new IllegalArgumentException("name contains non-ascii character: " + var0);
            }
         }

      }
   }

   static void validateHeaderValue(String var0) {
      if(var0 == null) {
         throw new NullPointerException("value");
      } else {
         for(int var1 = 0; var1 < var0.length(); ++var1) {
            char var2 = var0.charAt(var1);
            if(var2 == 0) {
               throw new IllegalArgumentException("value contains null character: " + var0);
            }
         }

      }
   }
}
