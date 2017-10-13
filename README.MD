# Comparing performance of Java I/O and NIO: streams vs channels

## What is this?
The new I/O library was introduced in SDK 1.4 providing *block-oriented* I/O. Before introduction 
of NIO, I/O has been carried out using *stream* metaphor where a stream deals with data one byte at 
a time. An input stream produces one byte of data, and an output stream consumes one byte of data. In 
contrast to that, block-oriented I/O deals with a block of data in on one step. So the goal of this 
is to run typical I/O on different systems and compare throughput of streams vs channels on sequential read/write.

## How it is done?
The repository contains `TestHarness` that runs all `Testable` N times and measures execution time of each 
run of each `Testable`. Then some statistical parameters are computed and printed to stdout.

## What was implemented?
Four following ways of reading and writing to a file were implemented:
##### Java I/O (streams):
* BufferedInputStream/BufferedOutputStream
##### Java NIO (channels):
* Memory-mapped files 
* Direct ByteBuffer
* Indirect ByteBuffer

## What are the experiments conducted?
Each input mechanism is tested on reading of 1GB file using a buffer of a fixed size (1024 bytes and 8192 bytes). Each output 
mechanism is tested on generating of 1GB file by writing the same size buffer at a time. The buffer contains one byte 
repeated many times.
 
One experiment test all I/O means in the following sequence on a particular system:
* DirectByteBufferGenerator
* DirectByteBufferInput
* StreamsBasedGenerator
* StreamsBasedInput
* IndirectByteBufferGenerator
* IndirectByteBufferInput
* MemoryMappedGenerator
* MemoryMappedInput

Each step runs 50 times. Based on the results of the experiments we discuss common Java I/O 
programming beliefs below.  

## Direct byte buffer is faster than indirect?
Figures below depict aggregated results for direct and indirect (default) byte buffers I/O. 
As you can see, direct byte buffer almost always a little bit faster that indirect byte buffer.
![](https://github.com/romromov/java-io-benchmark/tree/master/img/bb_write.png)
![](https://github.com/romromov/java-io-benchmark/tree/master/img/bb_read.png)
![](https://github.com/romromov/java-io-benchmark/tree/master/img/bb_read_write.png)

It is also clear that increasing of buffer size from 1K to 8K gives speed improvement for all 3 benchmarked systems.

Let's take a more detailed look on the same data. Figures below show the speed of each (out of 50) runs.
It is interesting to notice that plots for ssd2 system are heavily intertwined. Sometimes 
direct byte buffer is faster than it's counter-part, sometimes-the other way around. For 
ssd1 and hdd1 the distinction is more clearly.

![](https://github.com/romromov/java-io-benchmark/tree/master/img/hdd1_w_1k.png)
![](https://github.com/romromov/java-io-benchmark/tree/master/img/hdd1_w_8k.png)
![](https://github.com/romromov/java-io-benchmark/tree/master/img/ssd1_w_1k.png)
![](https://github.com/romromov/java-io-benchmark/tree/master/img/ssd1_w_8k.png)
![](https://github.com/romromov/java-io-benchmark/tree/master/img/ssd2_w_1k.png)
![](https://github.com/romromov/java-io-benchmark/tree/master/img/ssd2_w_8k.png)

## Does direct byte buffer has additional allocation cost?
It's believed that using direct byte buffer has additional cost comparing to indirect 
byte buffer. The cost is associated with first buffer allocation. From the images above, 
we conclude that the claim is supported only by one system SSD1 out of three, where you can easily 
notice 10% drop for the first run.  

## Channels faster than streams?
On the figures below you can see the average I/O speed of all compared approaches from 
which we can conclude the following:
- For 8K buffers, channels (direct and indirect byte buffer) are faster than streams. For 1K buffer
we can observe the opposite sometimes.

![](https://github.com/romromov/java-io-benchmark/tree/master/img/all_w.png)
![](https://github.com/romromov/java-io-benchmark/tree/master/img/all_r.png)

## Memory-mapped files is the fastest approach to I/O?
Well, memory-mapped files are tricky. Sometimes this way of writing is orders of magnitude faster than channels,
 sometimes slower. In our experiments it was always faster than streams.

In general, reading with memory mapped files is significantly faster than other ways, but sometimes
  can a bit slower that channels.

For some reasons, the weakest system ssd2 has demonstrated the best memory-mapped write speed.
It won by a far margin of 3-4.5X.
  
## Summary
What we have seen in our small survey, is that channels are faster than streams if buffer size is selected appropriately.
Direct byte buffers are faster than indirect ones. Also we cannot confirm a common opinion that first opening of 
a direct byte buffer is always slower that the following. This was only seen on hdd1 system.

It is clear that I/O performance heavily depends on configuration of the system. In particular, cache size,
file system page size, RAM etc. If you need high performance consequent I/O on Java you need to experiment and compare.
I would consider approaches according to the following priority:
- Memory-mapped files
- Direct ByteBuffer
- Indirect ByteBuffer / streams

## Systems under the test
The experiments were conducted on 3 following systems:

Name | Storage | CPU | OS | RAM
-----|------------------------|----------------------------------------------------|-------------------|----
ssd_2|Samsung SSD PM830 128GB |	Intel Core i5-2540M  @ 2.6 GHz(2 cores)	| Windows 10 Enterprise 64-bit | RAM 4GB
ssd_1|ATA Samsung SSD 850 |	Intel Xeon E5620 @ 2.4GHz (4 cores)	Ubuntu 64 bit | RAM 18GB
hdd_1|ATA SEAGATE ST3500413AS |	Intel Xeon E5620 @ 2.4GHz (4 cores)	Ubuntu 64 bit |	RAM 18GB

Memory mapped files are in general faster than any other compared means of OI but rarely can be order of magnitude slower 
  than channels. 

## Contributing
You are welcome to contribute whatever you think will be helpful to fellow programmers. If you would 
like to contribute statistics for different systems, feel free build the project and run Shell class on you machine.
I used the following parameters in the experiments:
`java -jar java-io-benchmark.jar 1.txt 1073741824 1024 50` for 1K buffer and `java -jar java-io-benchmark.jar 1.txt 1073741824 8192 50` 
 for 8K buffer.

## Misc
Sometimes computed speed is beyond the physical limits of discs. For instance, ~7000 MB/s on hdd1_1024.
That is because the value was computed but not measured. We measured time to read 1GB file 
150.8 millis then we computed speed (MB/s): 1024 / 150.8 * 1000 = 6790. So the absolute values make a little sense 
but it allows to compare different I/O approaches to each other.
