package com.cbb.MyRPC.server.tcp;

import com.cbb.MyRPC.protocol.ProtocolConstants;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;



public class TcpBufferHandlerWrapper implements Handler<Buffer> {

    private final RecordParser recordParser;

    public TcpBufferHandlerWrapper(Handler<Buffer> bufferHandler) {
        this.recordParser = initRecordParser(bufferHandler);
    }

    @Override
    public void handle(Buffer buffer) {
        recordParser.handle(buffer);
    }

    private RecordParser initRecordParser(Handler<Buffer> BufferHandler) {
        RecordParser parser = RecordParser.newFixed(ProtocolConstants.MESSAGE_HEADER_LENGTH);
        parser.setOutput(new Handler<Buffer>(){
            int size = -1;

            Buffer resultBuffer = Buffer.buffer();
            
            @Override
            public void handle(Buffer buffer) {
                if(size == -1) {
                    size = buffer.getInt(13);
                    recordParser.fixedSizeMode(size);
                    resultBuffer.appendBytes(buffer.getBytes());
                }else {
                    resultBuffer.appendBytes(buffer.getBytes());
                    BufferHandler.handle(resultBuffer);
                    recordParser.fixedSizeMode(ProtocolConstants.MESSAGE_HEADER_LENGTH);
                    size = -1;
                    resultBuffer = Buffer.buffer();
                }
            }
        });
        return parser;
    }
}
