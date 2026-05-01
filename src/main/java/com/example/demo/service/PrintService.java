package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.InetSocketAddress;
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

			} catch (Exception e) {
				log.error("Print error", e);
			}
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
			try (Socket socket = new Socket("192.168.1.10", 9100); OutputStream out = socket.getOutputStream()) {
				out.write(new byte[] { 0x1B, 0x40 }); // init
				out.write(receiptText.getBytes(StandardCharsets.US_ASCII));
				out.write("\n\n\n".getBytes(StandardCharsets.US_ASCII));
				out.write(new byte[] { 0x1D, 0x56, 0x41, 0x10 }); // cut
				out.flush();

			} catch (Exception e) {
				log.error("Print error", e);
			}

    }
    
    public void printReceiptCopies(String receiptText) {
        if (!printEnabled) {
            log.info("Print disabled (print.enabled=false)");
            return;
        }

        for (int i = 0; i < 2; i++) {
            printSingleReceipt(receiptText);

            if (i == 0) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    private void printSingleReceipt(String receiptText) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("192.168.1.10", 9100), 1000);
            socket.setSoTimeout(1000);

            try (OutputStream out = socket.getOutputStream()) {
                out.write(new byte[] { 0x1B, 0x40 });
                out.write(receiptText.getBytes(StandardCharsets.US_ASCII));
                out.write("\n\n\n\n\n".getBytes(StandardCharsets.US_ASCII));
                out.write(new byte[] { 0x1D, 0x56, 0x41, 0x10 });
                out.flush();
            }

        } catch (Exception e) {
            log.error("Print error", e);
        }
    }
}
