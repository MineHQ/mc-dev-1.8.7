package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.CaseIgnoringComparator;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostBodyUtil;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HttpPostRequestDecoder {
   private static final int DEFAULT_DISCARD_THRESHOLD = 10485760;
   private final HttpDataFactory factory;
   private final HttpRequest request;
   private final Charset charset;
   private boolean bodyToDecode;
   private boolean isLastChunk;
   private final List<InterfaceHttpData> bodyListHttpData;
   private final Map<String, List<InterfaceHttpData>> bodyMapHttpData;
   private ByteBuf undecodedChunk;
   private boolean isMultipart;
   private int bodyListHttpDataRank;
   private String multipartDataBoundary;
   private String multipartMixedBoundary;
   private HttpPostRequestDecoder.MultiPartStatus currentStatus;
   private Map<String, Attribute> currentFieldAttributes;
   private FileUpload currentFileUpload;
   private Attribute currentAttribute;
   private boolean destroyed;
   private int discardThreshold;

   public HttpPostRequestDecoder(HttpRequest var1) throws HttpPostRequestDecoder.ErrorDataDecoderException, HttpPostRequestDecoder.IncompatibleDataDecoderException {
      this(new DefaultHttpDataFactory(16384L), var1, HttpConstants.DEFAULT_CHARSET);
   }

   public HttpPostRequestDecoder(HttpDataFactory var1, HttpRequest var2) throws HttpPostRequestDecoder.ErrorDataDecoderException, HttpPostRequestDecoder.IncompatibleDataDecoderException {
      this(var1, var2, HttpConstants.DEFAULT_CHARSET);
   }

   public HttpPostRequestDecoder(HttpDataFactory var1, HttpRequest var2, Charset var3) throws HttpPostRequestDecoder.ErrorDataDecoderException, HttpPostRequestDecoder.IncompatibleDataDecoderException {
      this.bodyListHttpData = new ArrayList();
      this.bodyMapHttpData = new TreeMap(CaseIgnoringComparator.INSTANCE);
      this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.NOTSTARTED;
      this.discardThreshold = 10485760;
      if(var1 == null) {
         throw new NullPointerException("factory");
      } else if(var2 == null) {
         throw new NullPointerException("request");
      } else if(var3 == null) {
         throw new NullPointerException("charset");
      } else {
         this.request = var2;
         HttpMethod var4 = var2.getMethod();
         if(var4.equals(HttpMethod.POST) || var4.equals(HttpMethod.PUT) || var4.equals(HttpMethod.PATCH)) {
            this.bodyToDecode = true;
         }

         this.charset = var3;
         this.factory = var1;
         String var5 = this.request.headers().get("Content-Type");
         if(var5 != null) {
            this.checkMultipart(var5);
         } else {
            this.isMultipart = false;
         }

         if(!this.bodyToDecode) {
            throw new HttpPostRequestDecoder.IncompatibleDataDecoderException("No Body to decode");
         } else {
            if(var2 instanceof HttpContent) {
               this.offer((HttpContent)var2);
            } else {
               this.undecodedChunk = Unpooled.buffer();
               this.parseBody();
            }

         }
      }
   }

   private void checkMultipart(String var1) throws HttpPostRequestDecoder.ErrorDataDecoderException {
      String[] var2 = splitHeaderContentType(var1);
      if(var2[0].toLowerCase().startsWith("multipart/form-data") && var2[1].toLowerCase().startsWith("boundary")) {
         String[] var3 = StringUtil.split(var2[1], '=');
         if(var3.length != 2) {
            throw new HttpPostRequestDecoder.ErrorDataDecoderException("Needs a boundary value");
         }

         if(var3[1].charAt(0) == 34) {
            String var4 = var3[1].trim();
            int var5 = var4.length() - 1;
            if(var4.charAt(var5) == 34) {
               var3[1] = var4.substring(1, var5);
            }
         }

         this.multipartDataBoundary = "--" + var3[1];
         this.isMultipart = true;
         this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER;
      } else {
         this.isMultipart = false;
      }

   }

   private void checkDestroyed() {
      if(this.destroyed) {
         throw new IllegalStateException(HttpPostRequestDecoder.class.getSimpleName() + " was destroyed already");
      }
   }

   public boolean isMultipart() {
      this.checkDestroyed();
      return this.isMultipart;
   }

   public void setDiscardThreshold(int var1) {
      if(var1 < 0) {
         throw new IllegalArgumentException("discardThreshold must be >= 0");
      } else {
         this.discardThreshold = var1;
      }
   }

   public int getDiscardThreshold() {
      return this.discardThreshold;
   }

   public List<InterfaceHttpData> getBodyHttpDatas() throws HttpPostRequestDecoder.NotEnoughDataDecoderException {
      this.checkDestroyed();
      if(!this.isLastChunk) {
         throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
      } else {
         return this.bodyListHttpData;
      }
   }

   public List<InterfaceHttpData> getBodyHttpDatas(String var1) throws HttpPostRequestDecoder.NotEnoughDataDecoderException {
      this.checkDestroyed();
      if(!this.isLastChunk) {
         throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
      } else {
         return (List)this.bodyMapHttpData.get(var1);
      }
   }

   public InterfaceHttpData getBodyHttpData(String var1) throws HttpPostRequestDecoder.NotEnoughDataDecoderException {
      this.checkDestroyed();
      if(!this.isLastChunk) {
         throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
      } else {
         List var2 = (List)this.bodyMapHttpData.get(var1);
         return var2 != null?(InterfaceHttpData)var2.get(0):null;
      }
   }

   public HttpPostRequestDecoder offer(HttpContent var1) throws HttpPostRequestDecoder.ErrorDataDecoderException {
      this.checkDestroyed();
      ByteBuf var2 = var1.content();
      if(this.undecodedChunk == null) {
         this.undecodedChunk = var2.copy();
      } else {
         this.undecodedChunk.writeBytes(var2);
      }

      if(var1 instanceof LastHttpContent) {
         this.isLastChunk = true;
      }

      this.parseBody();
      if(this.undecodedChunk != null && this.undecodedChunk.writerIndex() > this.discardThreshold) {
         this.undecodedChunk.discardReadBytes();
      }

      return this;
   }

   public boolean hasNext() throws HttpPostRequestDecoder.EndOfDataDecoderException {
      this.checkDestroyed();
      if(this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.EPILOGUE && this.bodyListHttpDataRank >= this.bodyListHttpData.size()) {
         throw new HttpPostRequestDecoder.EndOfDataDecoderException();
      } else {
         return !this.bodyListHttpData.isEmpty() && this.bodyListHttpDataRank < this.bodyListHttpData.size();
      }
   }

   public InterfaceHttpData next() throws HttpPostRequestDecoder.EndOfDataDecoderException {
      this.checkDestroyed();
      return this.hasNext()?(InterfaceHttpData)this.bodyListHttpData.get(this.bodyListHttpDataRank++):null;
   }

   private void parseBody() throws HttpPostRequestDecoder.ErrorDataDecoderException {
      if(this.currentStatus != HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE && this.currentStatus != HttpPostRequestDecoder.MultiPartStatus.EPILOGUE) {
         if(this.isMultipart) {
            this.parseBodyMultipart();
         } else {
            this.parseBodyAttributes();
         }

      } else {
         if(this.isLastChunk) {
            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.EPILOGUE;
         }

      }
   }

   protected void addHttpData(InterfaceHttpData var1) {
      if(var1 != null) {
         Object var2 = (List)this.bodyMapHttpData.get(var1.getName());
         if(var2 == null) {
            var2 = new ArrayList(1);
            this.bodyMapHttpData.put(var1.getName(), var2);
         }

         ((List)var2).add(var1);
         this.bodyListHttpData.add(var1);
      }
   }

   private void parseBodyAttributesStandard() throws HttpPostRequestDecoder.ErrorDataDecoderException {
      int var1 = this.undecodedChunk.readerIndex();
      int var2 = var1;
      if(this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.NOTSTARTED) {
         this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
      }

      boolean var5 = true;

      try {
         while(this.undecodedChunk.isReadable() && var5) {
            char var6 = (char)this.undecodedChunk.readUnsignedByte();
            ++var2;
            int var4;
            switch(HttpPostRequestDecoder.SyntheticClass_1.$SwitchMap$io$netty$handler$codec$http$multipart$HttpPostRequestDecoder$MultiPartStatus[this.currentStatus.ordinal()]) {
            case 1:
               String var7;
               if(var6 == 61) {
                  this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.FIELD;
                  int var3 = var2 - 1;
                  var7 = decodeAttribute(this.undecodedChunk.toString(var1, var3 - var1, this.charset), this.charset);
                  this.currentAttribute = this.factory.createAttribute(this.request, var7);
                  var1 = var2;
               } else if(var6 == 38) {
                  this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
                  var4 = var2 - 1;
                  var7 = decodeAttribute(this.undecodedChunk.toString(var1, var4 - var1, this.charset), this.charset);
                  this.currentAttribute = this.factory.createAttribute(this.request, var7);
                  this.currentAttribute.setValue("");
                  this.addHttpData(this.currentAttribute);
                  this.currentAttribute = null;
                  var1 = var2;
                  var5 = true;
               }
               break;
            case 2:
               if(var6 == 38) {
                  this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
                  var4 = var2 - 1;
                  this.setFinalBuffer(this.undecodedChunk.copy(var1, var4 - var1));
                  var1 = var2;
                  var5 = true;
               } else if(var6 == 13) {
                  if(this.undecodedChunk.isReadable()) {
                     var6 = (char)this.undecodedChunk.readUnsignedByte();
                     ++var2;
                     if(var6 != 10) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException("Bad end of line");
                     }

                     this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE;
                     var4 = var2 - 2;
                     this.setFinalBuffer(this.undecodedChunk.copy(var1, var4 - var1));
                     var1 = var2;
                     var5 = false;
                  } else {
                     --var2;
                  }
               } else if(var6 == 10) {
                  this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE;
                  var4 = var2 - 1;
                  this.setFinalBuffer(this.undecodedChunk.copy(var1, var4 - var1));
                  var1 = var2;
                  var5 = false;
               }
               break;
            default:
               var5 = false;
            }
         }

         if(this.isLastChunk && this.currentAttribute != null) {
            if(var2 > var1) {
               this.setFinalBuffer(this.undecodedChunk.copy(var1, var2 - var1));
            } else if(!this.currentAttribute.isCompleted()) {
               this.setFinalBuffer(Unpooled.EMPTY_BUFFER);
            }

            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.EPILOGUE;
            this.undecodedChunk.readerIndex(var2);
         } else {
            if(var5 && this.currentAttribute != null) {
               if(this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.FIELD) {
                  this.currentAttribute.addContent(this.undecodedChunk.copy(var1, var2 - var1), false);
                  var1 = var2;
               }

               this.undecodedChunk.readerIndex(var1);
            } else {
               this.undecodedChunk.readerIndex(var1);
            }

         }
      } catch (HttpPostRequestDecoder.ErrorDataDecoderException var8) {
         this.undecodedChunk.readerIndex(var1);
         throw var8;
      } catch (IOException var9) {
         this.undecodedChunk.readerIndex(var1);
         throw new HttpPostRequestDecoder.ErrorDataDecoderException(var9);
      }
   }

   private void parseBodyAttributes() throws HttpPostRequestDecoder.ErrorDataDecoderException {
      HttpPostBodyUtil.SeekAheadOptimize var1;
      try {
         var1 = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
      } catch (HttpPostBodyUtil.SeekAheadNoBackArrayException var9) {
         this.parseBodyAttributesStandard();
         return;
      }

      int var2 = this.undecodedChunk.readerIndex();
      int var3 = var2;
      if(this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.NOTSTARTED) {
         this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
      }

      boolean var6 = true;

      try {
         label87:
         while(var1.pos < var1.limit) {
            char var7 = (char)(var1.bytes[var1.pos++] & 255);
            ++var3;
            int var5;
            switch(HttpPostRequestDecoder.SyntheticClass_1.$SwitchMap$io$netty$handler$codec$http$multipart$HttpPostRequestDecoder$MultiPartStatus[this.currentStatus.ordinal()]) {
            case 1:
               String var8;
               if(var7 == 61) {
                  this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.FIELD;
                  int var4 = var3 - 1;
                  var8 = decodeAttribute(this.undecodedChunk.toString(var2, var4 - var2, this.charset), this.charset);
                  this.currentAttribute = this.factory.createAttribute(this.request, var8);
                  var2 = var3;
               } else if(var7 == 38) {
                  this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
                  var5 = var3 - 1;
                  var8 = decodeAttribute(this.undecodedChunk.toString(var2, var5 - var2, this.charset), this.charset);
                  this.currentAttribute = this.factory.createAttribute(this.request, var8);
                  this.currentAttribute.setValue("");
                  this.addHttpData(this.currentAttribute);
                  this.currentAttribute = null;
                  var2 = var3;
                  var6 = true;
               }
               break;
            case 2:
               if(var7 == 38) {
                  this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
                  var5 = var3 - 1;
                  this.setFinalBuffer(this.undecodedChunk.copy(var2, var5 - var2));
                  var2 = var3;
                  var6 = true;
               } else if(var7 == 13) {
                  if(var1.pos < var1.limit) {
                     var7 = (char)(var1.bytes[var1.pos++] & 255);
                     ++var3;
                     if(var7 != 10) {
                        var1.setReadPosition(0);
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException("Bad end of line");
                     }

                     this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE;
                     var5 = var3 - 2;
                     var1.setReadPosition(0);
                     this.setFinalBuffer(this.undecodedChunk.copy(var2, var5 - var2));
                     var2 = var3;
                     var6 = false;
                     break label87;
                  }

                  if(var1.limit > 0) {
                     --var3;
                  }
               } else {
                  if(var7 != 10) {
                     continue;
                  }

                  this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE;
                  var5 = var3 - 1;
                  var1.setReadPosition(0);
                  this.setFinalBuffer(this.undecodedChunk.copy(var2, var5 - var2));
                  var2 = var3;
                  var6 = false;
                  break label87;
               }
               break;
            default:
               var1.setReadPosition(0);
               var6 = false;
               break label87;
            }
         }

         if(this.isLastChunk && this.currentAttribute != null) {
            if(var3 > var2) {
               this.setFinalBuffer(this.undecodedChunk.copy(var2, var3 - var2));
            } else if(!this.currentAttribute.isCompleted()) {
               this.setFinalBuffer(Unpooled.EMPTY_BUFFER);
            }

            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.EPILOGUE;
            this.undecodedChunk.readerIndex(var3);
         } else {
            if(var6 && this.currentAttribute != null) {
               if(this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.FIELD) {
                  this.currentAttribute.addContent(this.undecodedChunk.copy(var2, var3 - var2), false);
                  var2 = var3;
               }

               this.undecodedChunk.readerIndex(var2);
            } else {
               this.undecodedChunk.readerIndex(var2);
            }

         }
      } catch (HttpPostRequestDecoder.ErrorDataDecoderException var10) {
         this.undecodedChunk.readerIndex(var2);
         throw var10;
      } catch (IOException var11) {
         this.undecodedChunk.readerIndex(var2);
         throw new HttpPostRequestDecoder.ErrorDataDecoderException(var11);
      }
   }

   private void setFinalBuffer(ByteBuf var1) throws HttpPostRequestDecoder.ErrorDataDecoderException, IOException {
      this.currentAttribute.addContent(var1, true);
      String var2 = decodeAttribute(this.currentAttribute.getByteBuf().toString(this.charset), this.charset);
      this.currentAttribute.setValue(var2);
      this.addHttpData(this.currentAttribute);
      this.currentAttribute = null;
   }

   private static String decodeAttribute(String var0, Charset var1) throws HttpPostRequestDecoder.ErrorDataDecoderException {
      try {
         return QueryStringDecoder.decodeComponent(var0, var1);
      } catch (IllegalArgumentException var3) {
         throw new HttpPostRequestDecoder.ErrorDataDecoderException("Bad string: \'" + var0 + '\'', var3);
      }
   }

   private void parseBodyMultipart() throws HttpPostRequestDecoder.ErrorDataDecoderException {
      if(this.undecodedChunk != null && this.undecodedChunk.readableBytes() != 0) {
         for(InterfaceHttpData var1 = this.decodeMultipart(this.currentStatus); var1 != null; var1 = this.decodeMultipart(this.currentStatus)) {
            this.addHttpData(var1);
            if(this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE || this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.EPILOGUE) {
               break;
            }
         }

      }
   }

   private InterfaceHttpData decodeMultipart(HttpPostRequestDecoder.MultiPartStatus var1) throws HttpPostRequestDecoder.ErrorDataDecoderException {
      switch(HttpPostRequestDecoder.SyntheticClass_1.$SwitchMap$io$netty$handler$codec$http$multipart$HttpPostRequestDecoder$MultiPartStatus[var1.ordinal()]) {
      case 1:
         return this.findMultipartDisposition();
      case 2:
         Charset var2 = null;
         Attribute var3 = (Attribute)this.currentFieldAttributes.get("charset");
         if(var3 != null) {
            try {
               var2 = Charset.forName(var3.getValue());
            } catch (IOException var10) {
               throw new HttpPostRequestDecoder.ErrorDataDecoderException(var10);
            }
         }

         Attribute var4 = (Attribute)this.currentFieldAttributes.get("name");
         if(this.currentAttribute == null) {
            try {
               this.currentAttribute = this.factory.createAttribute(this.request, cleanString(var4.getValue()));
            } catch (NullPointerException var7) {
               throw new HttpPostRequestDecoder.ErrorDataDecoderException(var7);
            } catch (IllegalArgumentException var8) {
               throw new HttpPostRequestDecoder.ErrorDataDecoderException(var8);
            } catch (IOException var9) {
               throw new HttpPostRequestDecoder.ErrorDataDecoderException(var9);
            }

            if(var2 != null) {
               this.currentAttribute.setCharset(var2);
            }
         }

         try {
            this.loadFieldMultipart(this.multipartDataBoundary);
         } catch (HttpPostRequestDecoder.NotEnoughDataDecoderException var6) {
            return null;
         }

         Attribute var5 = this.currentAttribute;
         this.currentAttribute = null;
         this.currentFieldAttributes = null;
         this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER;
         return var5;
      case 3:
         throw new HttpPostRequestDecoder.ErrorDataDecoderException("Should not be called with the current getStatus");
      case 4:
         throw new HttpPostRequestDecoder.ErrorDataDecoderException("Should not be called with the current getStatus");
      case 5:
         return this.findMultipartDelimiter(this.multipartDataBoundary, HttpPostRequestDecoder.MultiPartStatus.DISPOSITION, HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE);
      case 6:
         return this.getFileUpload(this.multipartDataBoundary);
      case 7:
         return this.findMultipartDelimiter(this.multipartMixedBoundary, HttpPostRequestDecoder.MultiPartStatus.MIXEDDISPOSITION, HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER);
      case 8:
         return this.findMultipartDisposition();
      case 9:
         return this.getFileUpload(this.multipartMixedBoundary);
      case 10:
         return null;
      case 11:
         return null;
      default:
         throw new HttpPostRequestDecoder.ErrorDataDecoderException("Shouldn\'t reach here.");
      }
   }

   void skipControlCharacters() throws HttpPostRequestDecoder.NotEnoughDataDecoderException {
      HttpPostBodyUtil.SeekAheadOptimize var1;
      try {
         var1 = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
      } catch (HttpPostBodyUtil.SeekAheadNoBackArrayException var5) {
         try {
            this.skipControlCharactersStandard();
            return;
         } catch (IndexOutOfBoundsException var4) {
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(var4);
         }
      }

      char var2;
      do {
         if(var1.pos >= var1.limit) {
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException("Access out of bounds");
         }

         var2 = (char)(var1.bytes[var1.pos++] & 255);
      } while(Character.isISOControl(var2) || Character.isWhitespace(var2));

      var1.setReadPosition(1);
   }

   void skipControlCharactersStandard() {
      char var1;
      do {
         var1 = (char)this.undecodedChunk.readUnsignedByte();
      } while(Character.isISOControl(var1) || Character.isWhitespace(var1));

      this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
   }

   private InterfaceHttpData findMultipartDelimiter(String var1, HttpPostRequestDecoder.MultiPartStatus var2, HttpPostRequestDecoder.MultiPartStatus var3) throws HttpPostRequestDecoder.ErrorDataDecoderException {
      int var4 = this.undecodedChunk.readerIndex();

      try {
         this.skipControlCharacters();
      } catch (HttpPostRequestDecoder.NotEnoughDataDecoderException var8) {
         this.undecodedChunk.readerIndex(var4);
         return null;
      }

      this.skipOneLine();

      String var5;
      try {
         var5 = this.readDelimiter(var1);
      } catch (HttpPostRequestDecoder.NotEnoughDataDecoderException var7) {
         this.undecodedChunk.readerIndex(var4);
         return null;
      }

      if(var5.equals(var1)) {
         this.currentStatus = var2;
         return this.decodeMultipart(var2);
      } else if(var5.equals(var1 + "--")) {
         this.currentStatus = var3;
         if(this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER) {
            this.currentFieldAttributes = null;
            return this.decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER);
         } else {
            return null;
         }
      } else {
         this.undecodedChunk.readerIndex(var4);
         throw new HttpPostRequestDecoder.ErrorDataDecoderException("No Multipart delimiter found");
      }
   }

   private InterfaceHttpData findMultipartDisposition() throws HttpPostRequestDecoder.ErrorDataDecoderException {
      int var1 = this.undecodedChunk.readerIndex();
      if(this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.DISPOSITION) {
         this.currentFieldAttributes = new TreeMap(CaseIgnoringComparator.INSTANCE);
      }

      while(!this.skipOneLine()) {
         String var2;
         try {
            this.skipControlCharacters();
            var2 = this.readLine();
         } catch (HttpPostRequestDecoder.NotEnoughDataDecoderException var20) {
            this.undecodedChunk.readerIndex(var1);
            return null;
         }

         String[] var3 = splitMultipartHeader(var2);
         if(!var3[0].equalsIgnoreCase("Content-Disposition")) {
            Attribute var25;
            if(var3[0].equalsIgnoreCase("Content-Transfer-Encoding")) {
               try {
                  var25 = this.factory.createAttribute(this.request, "Content-Transfer-Encoding", cleanString(var3[1]));
               } catch (NullPointerException var16) {
                  throw new HttpPostRequestDecoder.ErrorDataDecoderException(var16);
               } catch (IllegalArgumentException var17) {
                  throw new HttpPostRequestDecoder.ErrorDataDecoderException(var17);
               }

               this.currentFieldAttributes.put("Content-Transfer-Encoding", var25);
            } else if(var3[0].equalsIgnoreCase("Content-Length")) {
               try {
                  var25 = this.factory.createAttribute(this.request, "Content-Length", cleanString(var3[1]));
               } catch (NullPointerException var14) {
                  throw new HttpPostRequestDecoder.ErrorDataDecoderException(var14);
               } catch (IllegalArgumentException var15) {
                  throw new HttpPostRequestDecoder.ErrorDataDecoderException(var15);
               }

               this.currentFieldAttributes.put("Content-Length", var25);
            } else {
               if(!var3[0].equalsIgnoreCase("Content-Type")) {
                  throw new HttpPostRequestDecoder.ErrorDataDecoderException("Unknown Params: " + var2);
               }

               if(var3[1].equalsIgnoreCase("multipart/mixed")) {
                  if(this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.DISPOSITION) {
                     String[] var23 = StringUtil.split(var3[2], '=');
                     this.multipartMixedBoundary = "--" + var23[1];
                     this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.MIXEDDELIMITER;
                     return this.decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.MIXEDDELIMITER);
                  }

                  throw new HttpPostRequestDecoder.ErrorDataDecoderException("Mixed Multipart found in a previous Mixed Multipart");
               }

               for(int var22 = 1; var22 < var3.length; ++var22) {
                  if(var3[var22].toLowerCase().startsWith("charset")) {
                     String[] var24 = StringUtil.split(var3[var22], '=');

                     Attribute var27;
                     try {
                        var27 = this.factory.createAttribute(this.request, "charset", cleanString(var24[1]));
                     } catch (NullPointerException var12) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(var12);
                     } catch (IllegalArgumentException var13) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(var13);
                     }

                     this.currentFieldAttributes.put("charset", var27);
                  } else {
                     Attribute var26;
                     try {
                        var26 = this.factory.createAttribute(this.request, cleanString(var3[0]), var3[var22]);
                     } catch (NullPointerException var10) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(var10);
                     } catch (IllegalArgumentException var11) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(var11);
                     }

                     this.currentFieldAttributes.put(var26.getName(), var26);
                  }
               }
            }
         } else {
            boolean var4;
            if(this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.DISPOSITION) {
               var4 = var3[1].equalsIgnoreCase("form-data");
            } else {
               var4 = var3[1].equalsIgnoreCase("attachment") || var3[1].equalsIgnoreCase("file");
            }

            if(var4) {
               for(int var5 = 2; var5 < var3.length; ++var5) {
                  String[] var6 = StringUtil.split(var3[var5], '=');

                  Attribute var7;
                  try {
                     String var8 = cleanString(var6[0]);
                     String var9 = var6[1];
                     if("filename".equals(var8)) {
                        var9 = var9.substring(1, var9.length() - 1);
                     } else {
                        var9 = cleanString(var9);
                     }

                     var7 = this.factory.createAttribute(this.request, var8, var9);
                  } catch (NullPointerException var18) {
                     throw new HttpPostRequestDecoder.ErrorDataDecoderException(var18);
                  } catch (IllegalArgumentException var19) {
                     throw new HttpPostRequestDecoder.ErrorDataDecoderException(var19);
                  }

                  this.currentFieldAttributes.put(var7.getName(), var7);
               }
            }
         }
      }

      Attribute var21 = (Attribute)this.currentFieldAttributes.get("filename");
      if(this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.DISPOSITION) {
         if(var21 != null) {
            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.FILEUPLOAD;
            return this.decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.FILEUPLOAD);
         } else {
            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.FIELD;
            return this.decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.FIELD);
         }
      } else if(var21 != null) {
         this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.MIXEDFILEUPLOAD;
         return this.decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.MIXEDFILEUPLOAD);
      } else {
         throw new HttpPostRequestDecoder.ErrorDataDecoderException("Filename not found");
      }
   }

   protected InterfaceHttpData getFileUpload(String var1) throws HttpPostRequestDecoder.ErrorDataDecoderException {
      Attribute var2 = (Attribute)this.currentFieldAttributes.get("Content-Transfer-Encoding");
      Charset var3 = this.charset;
      HttpPostBodyUtil.TransferEncodingMechanism var4 = HttpPostBodyUtil.TransferEncodingMechanism.BIT7;
      if(var2 != null) {
         String var5;
         try {
            var5 = var2.getValue().toLowerCase();
         } catch (IOException var20) {
            throw new HttpPostRequestDecoder.ErrorDataDecoderException(var20);
         }

         if(var5.equals(HttpPostBodyUtil.TransferEncodingMechanism.BIT7.value())) {
            var3 = HttpPostBodyUtil.US_ASCII;
         } else if(var5.equals(HttpPostBodyUtil.TransferEncodingMechanism.BIT8.value())) {
            var3 = HttpPostBodyUtil.ISO_8859_1;
            var4 = HttpPostBodyUtil.TransferEncodingMechanism.BIT8;
         } else {
            if(!var5.equals(HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value())) {
               throw new HttpPostRequestDecoder.ErrorDataDecoderException("TransferEncoding Unknown: " + var5);
            }

            var4 = HttpPostBodyUtil.TransferEncodingMechanism.BINARY;
         }
      }

      Attribute var21 = (Attribute)this.currentFieldAttributes.get("charset");
      if(var21 != null) {
         try {
            var3 = Charset.forName(var21.getValue());
         } catch (IOException var19) {
            throw new HttpPostRequestDecoder.ErrorDataDecoderException(var19);
         }
      }

      if(this.currentFileUpload == null) {
         Attribute var6 = (Attribute)this.currentFieldAttributes.get("filename");
         Attribute var7 = (Attribute)this.currentFieldAttributes.get("name");
         Attribute var8 = (Attribute)this.currentFieldAttributes.get("Content-Type");
         if(var8 == null) {
            throw new HttpPostRequestDecoder.ErrorDataDecoderException("Content-Type is absent but required");
         }

         Attribute var9 = (Attribute)this.currentFieldAttributes.get("Content-Length");

         long var10;
         try {
            var10 = var9 != null?Long.parseLong(var9.getValue()):0L;
         } catch (IOException var17) {
            throw new HttpPostRequestDecoder.ErrorDataDecoderException(var17);
         } catch (NumberFormatException var18) {
            var10 = 0L;
         }

         try {
            this.currentFileUpload = this.factory.createFileUpload(this.request, cleanString(var7.getValue()), cleanString(var6.getValue()), var8.getValue(), var4.value(), var3, var10);
         } catch (NullPointerException var14) {
            throw new HttpPostRequestDecoder.ErrorDataDecoderException(var14);
         } catch (IllegalArgumentException var15) {
            throw new HttpPostRequestDecoder.ErrorDataDecoderException(var15);
         } catch (IOException var16) {
            throw new HttpPostRequestDecoder.ErrorDataDecoderException(var16);
         }
      }

      try {
         this.readFileUploadByteMultipart(var1);
      } catch (HttpPostRequestDecoder.NotEnoughDataDecoderException var13) {
         return null;
      }

      if(this.currentFileUpload.isCompleted()) {
         if(this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.FILEUPLOAD) {
            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER;
            this.currentFieldAttributes = null;
         } else {
            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.MIXEDDELIMITER;
            this.cleanMixedAttributes();
         }

         FileUpload var22 = this.currentFileUpload;
         this.currentFileUpload = null;
         return var22;
      } else {
         return null;
      }
   }

   public void destroy() {
      this.checkDestroyed();
      this.cleanFiles();
      this.destroyed = true;
      if(this.undecodedChunk != null && this.undecodedChunk.refCnt() > 0) {
         this.undecodedChunk.release();
         this.undecodedChunk = null;
      }

      for(int var1 = this.bodyListHttpDataRank; var1 < this.bodyListHttpData.size(); ++var1) {
         ((InterfaceHttpData)this.bodyListHttpData.get(var1)).release();
      }

   }

   public void cleanFiles() {
      this.checkDestroyed();
      this.factory.cleanRequestHttpDatas(this.request);
   }

   public void removeHttpDataFromClean(InterfaceHttpData var1) {
      this.checkDestroyed();
      this.factory.removeHttpDataFromClean(this.request, var1);
   }

   private void cleanMixedAttributes() {
      this.currentFieldAttributes.remove("charset");
      this.currentFieldAttributes.remove("Content-Length");
      this.currentFieldAttributes.remove("Content-Transfer-Encoding");
      this.currentFieldAttributes.remove("Content-Type");
      this.currentFieldAttributes.remove("filename");
   }

   private String readLineStandard() throws HttpPostRequestDecoder.NotEnoughDataDecoderException {
      int var1 = this.undecodedChunk.readerIndex();

      try {
         ByteBuf var2 = Unpooled.buffer(64);

         while(this.undecodedChunk.isReadable()) {
            byte var3 = this.undecodedChunk.readByte();
            if(var3 == 13) {
               var3 = this.undecodedChunk.getByte(this.undecodedChunk.readerIndex());
               if(var3 == 10) {
                  this.undecodedChunk.skipBytes(1);
                  return var2.toString(this.charset);
               }

               var2.writeByte(13);
            } else {
               if(var3 == 10) {
                  return var2.toString(this.charset);
               }

               var2.writeByte(var3);
            }
         }
      } catch (IndexOutOfBoundsException var4) {
         this.undecodedChunk.readerIndex(var1);
         throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(var4);
      }

      this.undecodedChunk.readerIndex(var1);
      throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
   }

   private String readLine() throws HttpPostRequestDecoder.NotEnoughDataDecoderException {
      HttpPostBodyUtil.SeekAheadOptimize var1;
      try {
         var1 = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
      } catch (HttpPostBodyUtil.SeekAheadNoBackArrayException var5) {
         return this.readLineStandard();
      }

      int var2 = this.undecodedChunk.readerIndex();

      try {
         ByteBuf var3 = Unpooled.buffer(64);

         while(var1.pos < var1.limit) {
            byte var4 = var1.bytes[var1.pos++];
            if(var4 == 13) {
               if(var1.pos < var1.limit) {
                  var4 = var1.bytes[var1.pos++];
                  if(var4 == 10) {
                     var1.setReadPosition(0);
                     return var3.toString(this.charset);
                  }

                  --var1.pos;
                  var3.writeByte(13);
               } else {
                  var3.writeByte(var4);
               }
            } else {
               if(var4 == 10) {
                  var1.setReadPosition(0);
                  return var3.toString(this.charset);
               }

               var3.writeByte(var4);
            }
         }
      } catch (IndexOutOfBoundsException var6) {
         this.undecodedChunk.readerIndex(var2);
         throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(var6);
      }

      this.undecodedChunk.readerIndex(var2);
      throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
   }

   private String readDelimiterStandard(String var1) throws HttpPostRequestDecoder.NotEnoughDataDecoderException {
      int var2 = this.undecodedChunk.readerIndex();

      try {
         StringBuilder var3 = new StringBuilder(64);
         int var4 = 0;
         int var5 = var1.length();

         byte var6;
         while(this.undecodedChunk.isReadable() && var4 < var5) {
            var6 = this.undecodedChunk.readByte();
            if(var6 != var1.charAt(var4)) {
               this.undecodedChunk.readerIndex(var2);
               throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
            }

            ++var4;
            var3.append((char)var6);
         }

         if(this.undecodedChunk.isReadable()) {
            var6 = this.undecodedChunk.readByte();
            if(var6 == 13) {
               var6 = this.undecodedChunk.readByte();
               if(var6 == 10) {
                  return var3.toString();
               }

               this.undecodedChunk.readerIndex(var2);
               throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
            }

            if(var6 == 10) {
               return var3.toString();
            }

            if(var6 == 45) {
               var3.append('-');
               var6 = this.undecodedChunk.readByte();
               if(var6 == 45) {
                  var3.append('-');
                  if(this.undecodedChunk.isReadable()) {
                     var6 = this.undecodedChunk.readByte();
                     if(var6 == 13) {
                        var6 = this.undecodedChunk.readByte();
                        if(var6 == 10) {
                           return var3.toString();
                        }

                        this.undecodedChunk.readerIndex(var2);
                        throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                     }

                     if(var6 == 10) {
                        return var3.toString();
                     }

                     this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
                     return var3.toString();
                  }

                  return var3.toString();
               }
            }
         }
      } catch (IndexOutOfBoundsException var7) {
         this.undecodedChunk.readerIndex(var2);
         throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(var7);
      }

      this.undecodedChunk.readerIndex(var2);
      throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
   }

   private String readDelimiter(String var1) throws HttpPostRequestDecoder.NotEnoughDataDecoderException {
      HttpPostBodyUtil.SeekAheadOptimize var2;
      try {
         var2 = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
      } catch (HttpPostBodyUtil.SeekAheadNoBackArrayException var8) {
         return this.readDelimiterStandard(var1);
      }

      int var3 = this.undecodedChunk.readerIndex();
      int var4 = 0;
      int var5 = var1.length();

      try {
         StringBuilder var6 = new StringBuilder(64);

         byte var7;
         while(var2.pos < var2.limit && var4 < var5) {
            var7 = var2.bytes[var2.pos++];
            if(var7 != var1.charAt(var4)) {
               this.undecodedChunk.readerIndex(var3);
               throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
            }

            ++var4;
            var6.append((char)var7);
         }

         if(var2.pos < var2.limit) {
            var7 = var2.bytes[var2.pos++];
            if(var7 == 13) {
               if(var2.pos < var2.limit) {
                  var7 = var2.bytes[var2.pos++];
                  if(var7 == 10) {
                     var2.setReadPosition(0);
                     return var6.toString();
                  }

                  this.undecodedChunk.readerIndex(var3);
                  throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
               }

               this.undecodedChunk.readerIndex(var3);
               throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
            }

            if(var7 == 10) {
               var2.setReadPosition(0);
               return var6.toString();
            }

            if(var7 == 45) {
               var6.append('-');
               if(var2.pos < var2.limit) {
                  var7 = var2.bytes[var2.pos++];
                  if(var7 == 45) {
                     var6.append('-');
                     if(var2.pos < var2.limit) {
                        var7 = var2.bytes[var2.pos++];
                        if(var7 == 13) {
                           if(var2.pos < var2.limit) {
                              var7 = var2.bytes[var2.pos++];
                              if(var7 == 10) {
                                 var2.setReadPosition(0);
                                 return var6.toString();
                              }

                              this.undecodedChunk.readerIndex(var3);
                              throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                           }

                           this.undecodedChunk.readerIndex(var3);
                           throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                        }

                        if(var7 == 10) {
                           var2.setReadPosition(0);
                           return var6.toString();
                        }

                        var2.setReadPosition(1);
                        return var6.toString();
                     }

                     var2.setReadPosition(0);
                     return var6.toString();
                  }
               }
            }
         }
      } catch (IndexOutOfBoundsException var9) {
         this.undecodedChunk.readerIndex(var3);
         throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(var9);
      }

      this.undecodedChunk.readerIndex(var3);
      throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
   }

   private void readFileUploadByteMultipartStandard(String var1) throws HttpPostRequestDecoder.NotEnoughDataDecoderException, HttpPostRequestDecoder.ErrorDataDecoderException {
      int var2 = this.undecodedChunk.readerIndex();
      boolean var3 = true;
      int var4 = 0;
      int var5 = this.undecodedChunk.readerIndex();
      boolean var6 = false;

      while(this.undecodedChunk.isReadable()) {
         byte var7 = this.undecodedChunk.readByte();
         if(var3) {
            if(var7 == var1.codePointAt(var4)) {
               ++var4;
               if(var1.length() == var4) {
                  var6 = true;
                  break;
               }
            } else {
               var3 = false;
               var4 = 0;
               if(var7 == 13) {
                  if(this.undecodedChunk.isReadable()) {
                     var7 = this.undecodedChunk.readByte();
                     if(var7 == 10) {
                        var3 = true;
                        var4 = 0;
                        var5 = this.undecodedChunk.readerIndex() - 2;
                     } else {
                        var5 = this.undecodedChunk.readerIndex() - 1;
                        this.undecodedChunk.readerIndex(var5);
                     }
                  }
               } else if(var7 == 10) {
                  var3 = true;
                  var4 = 0;
                  var5 = this.undecodedChunk.readerIndex() - 1;
               } else {
                  var5 = this.undecodedChunk.readerIndex();
               }
            }
         } else if(var7 == 13) {
            if(this.undecodedChunk.isReadable()) {
               var7 = this.undecodedChunk.readByte();
               if(var7 == 10) {
                  var3 = true;
                  var4 = 0;
                  var5 = this.undecodedChunk.readerIndex() - 2;
               } else {
                  var5 = this.undecodedChunk.readerIndex() - 1;
                  this.undecodedChunk.readerIndex(var5);
               }
            }
         } else if(var7 == 10) {
            var3 = true;
            var4 = 0;
            var5 = this.undecodedChunk.readerIndex() - 1;
         } else {
            var5 = this.undecodedChunk.readerIndex();
         }
      }

      ByteBuf var11 = this.undecodedChunk.copy(var2, var5 - var2);
      if(var6) {
         try {
            this.currentFileUpload.addContent(var11, true);
            this.undecodedChunk.readerIndex(var5);
         } catch (IOException var9) {
            throw new HttpPostRequestDecoder.ErrorDataDecoderException(var9);
         }
      } else {
         try {
            this.currentFileUpload.addContent(var11, false);
            this.undecodedChunk.readerIndex(var5);
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
         } catch (IOException var10) {
            throw new HttpPostRequestDecoder.ErrorDataDecoderException(var10);
         }
      }
   }

   private void readFileUploadByteMultipart(String var1) throws HttpPostRequestDecoder.NotEnoughDataDecoderException, HttpPostRequestDecoder.ErrorDataDecoderException {
      HttpPostBodyUtil.SeekAheadOptimize var2;
      try {
         var2 = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
      } catch (HttpPostBodyUtil.SeekAheadNoBackArrayException var13) {
         this.readFileUploadByteMultipartStandard(var1);
         return;
      }

      int var3 = this.undecodedChunk.readerIndex();
      boolean var4 = true;
      int var5 = 0;
      int var6 = var2.pos;
      boolean var8 = false;

      while(var2.pos < var2.limit) {
         byte var9 = var2.bytes[var2.pos++];
         if(var4) {
            if(var9 == var1.codePointAt(var5)) {
               ++var5;
               if(var1.length() == var5) {
                  var8 = true;
                  break;
               }
            } else {
               var4 = false;
               var5 = 0;
               if(var9 == 13) {
                  if(var2.pos < var2.limit) {
                     var9 = var2.bytes[var2.pos++];
                     if(var9 == 10) {
                        var4 = true;
                        var5 = 0;
                        var6 = var2.pos - 2;
                     } else {
                        --var2.pos;
                        var6 = var2.pos;
                     }
                  }
               } else if(var9 == 10) {
                  var4 = true;
                  var5 = 0;
                  var6 = var2.pos - 1;
               } else {
                  var6 = var2.pos;
               }
            }
         } else if(var9 == 13) {
            if(var2.pos < var2.limit) {
               var9 = var2.bytes[var2.pos++];
               if(var9 == 10) {
                  var4 = true;
                  var5 = 0;
                  var6 = var2.pos - 2;
               } else {
                  --var2.pos;
                  var6 = var2.pos;
               }
            }
         } else if(var9 == 10) {
            var4 = true;
            var5 = 0;
            var6 = var2.pos - 1;
         } else {
            var6 = var2.pos;
         }
      }

      int var7 = var2.getReadPosition(var6);
      ByteBuf var14 = this.undecodedChunk.copy(var3, var7 - var3);
      if(var8) {
         try {
            this.currentFileUpload.addContent(var14, true);
            this.undecodedChunk.readerIndex(var7);
         } catch (IOException var11) {
            throw new HttpPostRequestDecoder.ErrorDataDecoderException(var11);
         }
      } else {
         try {
            this.currentFileUpload.addContent(var14, false);
            this.undecodedChunk.readerIndex(var7);
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
         } catch (IOException var12) {
            throw new HttpPostRequestDecoder.ErrorDataDecoderException(var12);
         }
      }
   }

   private void loadFieldMultipartStandard(String var1) throws HttpPostRequestDecoder.NotEnoughDataDecoderException, HttpPostRequestDecoder.ErrorDataDecoderException {
      int var2 = this.undecodedChunk.readerIndex();

      try {
         boolean var3 = true;
         int var4 = 0;
         int var5 = this.undecodedChunk.readerIndex();
         boolean var6 = false;

         while(this.undecodedChunk.isReadable()) {
            byte var7 = this.undecodedChunk.readByte();
            if(var3) {
               if(var7 == var1.codePointAt(var4)) {
                  ++var4;
                  if(var1.length() == var4) {
                     var6 = true;
                     break;
                  }
               } else {
                  var3 = false;
                  var4 = 0;
                  if(var7 == 13) {
                     if(this.undecodedChunk.isReadable()) {
                        var7 = this.undecodedChunk.readByte();
                        if(var7 == 10) {
                           var3 = true;
                           var4 = 0;
                           var5 = this.undecodedChunk.readerIndex() - 2;
                        } else {
                           var5 = this.undecodedChunk.readerIndex() - 1;
                           this.undecodedChunk.readerIndex(var5);
                        }
                     } else {
                        var5 = this.undecodedChunk.readerIndex() - 1;
                     }
                  } else if(var7 == 10) {
                     var3 = true;
                     var4 = 0;
                     var5 = this.undecodedChunk.readerIndex() - 1;
                  } else {
                     var5 = this.undecodedChunk.readerIndex();
                  }
               }
            } else if(var7 == 13) {
               if(this.undecodedChunk.isReadable()) {
                  var7 = this.undecodedChunk.readByte();
                  if(var7 == 10) {
                     var3 = true;
                     var4 = 0;
                     var5 = this.undecodedChunk.readerIndex() - 2;
                  } else {
                     var5 = this.undecodedChunk.readerIndex() - 1;
                     this.undecodedChunk.readerIndex(var5);
                  }
               } else {
                  var5 = this.undecodedChunk.readerIndex() - 1;
               }
            } else if(var7 == 10) {
               var3 = true;
               var4 = 0;
               var5 = this.undecodedChunk.readerIndex() - 1;
            } else {
               var5 = this.undecodedChunk.readerIndex();
            }
         }

         if(var6) {
            try {
               this.currentAttribute.addContent(this.undecodedChunk.copy(var2, var5 - var2), true);
            } catch (IOException var8) {
               throw new HttpPostRequestDecoder.ErrorDataDecoderException(var8);
            }

            this.undecodedChunk.readerIndex(var5);
         } else {
            try {
               this.currentAttribute.addContent(this.undecodedChunk.copy(var2, var5 - var2), false);
            } catch (IOException var9) {
               throw new HttpPostRequestDecoder.ErrorDataDecoderException(var9);
            }

            this.undecodedChunk.readerIndex(var5);
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
         }
      } catch (IndexOutOfBoundsException var10) {
         this.undecodedChunk.readerIndex(var2);
         throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(var10);
      }
   }

   private void loadFieldMultipart(String var1) throws HttpPostRequestDecoder.NotEnoughDataDecoderException, HttpPostRequestDecoder.ErrorDataDecoderException {
      HttpPostBodyUtil.SeekAheadOptimize var2;
      try {
         var2 = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
      } catch (HttpPostBodyUtil.SeekAheadNoBackArrayException var12) {
         this.loadFieldMultipartStandard(var1);
         return;
      }

      int var3 = this.undecodedChunk.readerIndex();

      try {
         boolean var4 = true;
         int var5 = 0;
         int var7 = var2.pos;
         boolean var8 = false;

         while(var2.pos < var2.limit) {
            byte var9 = var2.bytes[var2.pos++];
            if(var4) {
               if(var9 == var1.codePointAt(var5)) {
                  ++var5;
                  if(var1.length() == var5) {
                     var8 = true;
                     break;
                  }
               } else {
                  var4 = false;
                  var5 = 0;
                  if(var9 == 13) {
                     if(var2.pos < var2.limit) {
                        var9 = var2.bytes[var2.pos++];
                        if(var9 == 10) {
                           var4 = true;
                           var5 = 0;
                           var7 = var2.pos - 2;
                        } else {
                           --var2.pos;
                           var7 = var2.pos;
                        }
                     }
                  } else if(var9 == 10) {
                     var4 = true;
                     var5 = 0;
                     var7 = var2.pos - 1;
                  } else {
                     var7 = var2.pos;
                  }
               }
            } else if(var9 == 13) {
               if(var2.pos < var2.limit) {
                  var9 = var2.bytes[var2.pos++];
                  if(var9 == 10) {
                     var4 = true;
                     var5 = 0;
                     var7 = var2.pos - 2;
                  } else {
                     --var2.pos;
                     var7 = var2.pos;
                  }
               }
            } else if(var9 == 10) {
               var4 = true;
               var5 = 0;
               var7 = var2.pos - 1;
            } else {
               var7 = var2.pos;
            }
         }

         int var6 = var2.getReadPosition(var7);
         if(var8) {
            try {
               this.currentAttribute.addContent(this.undecodedChunk.copy(var3, var6 - var3), true);
            } catch (IOException var10) {
               throw new HttpPostRequestDecoder.ErrorDataDecoderException(var10);
            }

            this.undecodedChunk.readerIndex(var6);
         } else {
            try {
               this.currentAttribute.addContent(this.undecodedChunk.copy(var3, var6 - var3), false);
            } catch (IOException var11) {
               throw new HttpPostRequestDecoder.ErrorDataDecoderException(var11);
            }

            this.undecodedChunk.readerIndex(var6);
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
         }
      } catch (IndexOutOfBoundsException var13) {
         this.undecodedChunk.readerIndex(var3);
         throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(var13);
      }
   }

   private static String cleanString(String var0) {
      StringBuilder var1 = new StringBuilder(var0.length());

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         char var3 = var0.charAt(var2);
         if(var3 == 58) {
            var1.append(32);
         } else if(var3 == 44) {
            var1.append(32);
         } else if(var3 == 61) {
            var1.append(32);
         } else if(var3 == 59) {
            var1.append(32);
         } else if(var3 == 9) {
            var1.append(32);
         } else if(var3 != 34) {
            var1.append(var3);
         }
      }

      return var1.toString().trim();
   }

   private boolean skipOneLine() {
      if(!this.undecodedChunk.isReadable()) {
         return false;
      } else {
         byte var1 = this.undecodedChunk.readByte();
         if(var1 == 13) {
            if(!this.undecodedChunk.isReadable()) {
               this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
               return false;
            } else {
               var1 = this.undecodedChunk.readByte();
               if(var1 == 10) {
                  return true;
               } else {
                  this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 2);
                  return false;
               }
            }
         } else if(var1 == 10) {
            return true;
         } else {
            this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
            return false;
         }
      }
   }

   private static String[] splitHeaderContentType(String var0) {
      int var1 = HttpPostBodyUtil.findNonWhitespace(var0, 0);
      int var2 = var0.indexOf(59);
      if(var2 == -1) {
         return new String[]{var0, ""};
      } else {
         if(var0.charAt(var2 - 1) == 32) {
            --var2;
         }

         int var3 = HttpPostBodyUtil.findNonWhitespace(var0, var2 + 1);
         int var4 = HttpPostBodyUtil.findEndOfString(var0);
         return new String[]{var0.substring(var1, var2), var0.substring(var3, var4)};
      }
   }

   private static String[] splitMultipartHeader(String var0) {
      ArrayList var1 = new ArrayList(1);
      int var2 = HttpPostBodyUtil.findNonWhitespace(var0, 0);

      int var3;
      for(var3 = var2; var3 < var0.length(); ++var3) {
         char var7 = var0.charAt(var3);
         if(var7 == 58 || Character.isWhitespace(var7)) {
            break;
         }
      }

      int var4;
      for(var4 = var3; var4 < var0.length(); ++var4) {
         if(var0.charAt(var4) == 58) {
            ++var4;
            break;
         }
      }

      int var5 = HttpPostBodyUtil.findNonWhitespace(var0, var4);
      int var6 = HttpPostBodyUtil.findEndOfString(var0);
      var1.add(var0.substring(var2, var3));
      String var13 = var0.substring(var5, var6);
      String[] var8;
      if(var13.indexOf(59) >= 0) {
         var8 = StringUtil.split(var13, ';');
      } else {
         var8 = StringUtil.split(var13, ',');
      }

      String[] var9 = var8;
      int var10 = var8.length;

      for(int var11 = 0; var11 < var10; ++var11) {
         String var12 = var9[var11];
         var1.add(var12.trim());
      }

      var9 = new String[var1.size()];

      for(var10 = 0; var10 < var1.size(); ++var10) {
         var9[var10] = (String)var1.get(var10);
      }

      return var9;
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$io$netty$handler$codec$http$multipart$HttpPostRequestDecoder$MultiPartStatus = new int[HttpPostRequestDecoder.MultiPartStatus.values().length];

      static {
         try {
            $SwitchMap$io$netty$handler$codec$http$multipart$HttpPostRequestDecoder$MultiPartStatus[HttpPostRequestDecoder.MultiPartStatus.DISPOSITION.ordinal()] = 1;
         } catch (NoSuchFieldError var11) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$http$multipart$HttpPostRequestDecoder$MultiPartStatus[HttpPostRequestDecoder.MultiPartStatus.FIELD.ordinal()] = 2;
         } catch (NoSuchFieldError var10) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$http$multipart$HttpPostRequestDecoder$MultiPartStatus[HttpPostRequestDecoder.MultiPartStatus.NOTSTARTED.ordinal()] = 3;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$http$multipart$HttpPostRequestDecoder$MultiPartStatus[HttpPostRequestDecoder.MultiPartStatus.PREAMBLE.ordinal()] = 4;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$http$multipart$HttpPostRequestDecoder$MultiPartStatus[HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER.ordinal()] = 5;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$http$multipart$HttpPostRequestDecoder$MultiPartStatus[HttpPostRequestDecoder.MultiPartStatus.FILEUPLOAD.ordinal()] = 6;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$http$multipart$HttpPostRequestDecoder$MultiPartStatus[HttpPostRequestDecoder.MultiPartStatus.MIXEDDELIMITER.ordinal()] = 7;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$http$multipart$HttpPostRequestDecoder$MultiPartStatus[HttpPostRequestDecoder.MultiPartStatus.MIXEDDISPOSITION.ordinal()] = 8;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$http$multipart$HttpPostRequestDecoder$MultiPartStatus[HttpPostRequestDecoder.MultiPartStatus.MIXEDFILEUPLOAD.ordinal()] = 9;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$http$multipart$HttpPostRequestDecoder$MultiPartStatus[HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE.ordinal()] = 10;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$http$multipart$HttpPostRequestDecoder$MultiPartStatus[HttpPostRequestDecoder.MultiPartStatus.EPILOGUE.ordinal()] = 11;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static class IncompatibleDataDecoderException extends DecoderException {
      private static final long serialVersionUID = -953268047926250267L;

      public IncompatibleDataDecoderException() {
      }

      public IncompatibleDataDecoderException(String var1) {
         super(var1);
      }

      public IncompatibleDataDecoderException(Throwable var1) {
         super(var1);
      }

      public IncompatibleDataDecoderException(String var1, Throwable var2) {
         super(var1, var2);
      }
   }

   public static class ErrorDataDecoderException extends DecoderException {
      private static final long serialVersionUID = 5020247425493164465L;

      public ErrorDataDecoderException() {
      }

      public ErrorDataDecoderException(String var1) {
         super(var1);
      }

      public ErrorDataDecoderException(Throwable var1) {
         super(var1);
      }

      public ErrorDataDecoderException(String var1, Throwable var2) {
         super(var1, var2);
      }
   }

   public static class EndOfDataDecoderException extends DecoderException {
      private static final long serialVersionUID = 1336267941020800769L;

      public EndOfDataDecoderException() {
      }
   }

   public static class NotEnoughDataDecoderException extends DecoderException {
      private static final long serialVersionUID = -7846841864603865638L;

      public NotEnoughDataDecoderException() {
      }

      public NotEnoughDataDecoderException(String var1) {
         super(var1);
      }

      public NotEnoughDataDecoderException(Throwable var1) {
         super(var1);
      }

      public NotEnoughDataDecoderException(String var1, Throwable var2) {
         super(var1, var2);
      }
   }

   private static enum MultiPartStatus {
      NOTSTARTED,
      PREAMBLE,
      HEADERDELIMITER,
      DISPOSITION,
      FIELD,
      FILEUPLOAD,
      MIXEDPREAMBLE,
      MIXEDDELIMITER,
      MIXEDDISPOSITION,
      MIXEDFILEUPLOAD,
      MIXEDCLOSEDELIMITER,
      CLOSEDELIMITER,
      PREEPILOGUE,
      EPILOGUE;

      private MultiPartStatus() {
      }
   }
}
