package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Service
public class PrintService {
    private static final Logger log = LoggerFactory.getLogger(PrintService.class);

    @Value("${print.enabled:true}")
    private boolean printEnabled;

    
    public void printReceipt(String receiptText) {
        if (!printEnabled) {
            log.info("Print disabled (print.enabled=false)");
            return;
        }

			try (Socket socket = new Socket("192.168.1.10", 9100); OutputStream out = socket.getOutputStream()) {
				out.write(new byte[] { 0x1B, 0x40 }); // init
				out.write(receiptText.getBytes(StandardCharsets.US_ASCII));
				out.write("\n\n\n".getBytes(StandardCharsets.US_ASCII));
				out.write(new byte[] { 0x1D, 0x56, 0x41, 0x10 }); // cut
				out.flush();
				socket.close();
			} catch (Exception e) {
				log.error("Print error", e);
			}

			try (Socket socket = new Socket("192.168.1.10", 9100); OutputStream out = socket.getOutputStream()) {
				out.write(new byte[] { 0x1B, 0x40 }); // init
				out.write(receiptText.getBytes(StandardCharsets.US_ASCII));
				out.write("\n\n\n".getBytes(StandardCharsets.US_ASCII));
				out.write(new byte[] { 0x1D, 0x56, 0x41, 0x10 }); // cut
				out.flush();
				socket.close();
			} catch (Exception e) {
				log.error("Print error", e);
			}
    }
}
