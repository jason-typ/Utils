package com.data.logging.yarnTest.localTest.io

import java.io.{InputStream, OutputStream}
import java.util.zip.{ZipEntry, ZipInputStream, ZipOutputStream}

import org.joda.time.{DateTime, DateTimeZone}

class SimpleZipOutputFile(output: OutputStream) {
  val zipOutputStream = new ZipOutputStream(output)

  def write(filename: String, content: Array[Byte], time: Long): Unit = {
    val zipEntry = new ZipEntry(filename)
    zipEntry.setTime(time)

    zipOutputStream.putNextEntry(zipEntry)
    zipOutputStream.write(content)

    zipOutputStream.closeEntry()
  }

  def write(filename: String, content: Array[Byte]): Unit = {
    write(filename, content, DateTime.now(DateTimeZone.UTC).getMillis)
  }

  def close(): Unit = {
    zipOutputStream.close()
  }
}

class SimpleZipInputFile(input: InputStream) {
  val zipInputStream = new ZipInputStream(input)
  val buffer = new Array[Byte](1 << 24)

  def read(): Option[(String, Array[Byte])] = {
    val entry = zipInputStream.getNextEntry
    if (entry != null) {
      val name = entry.getName
      var bytesRead = 0
      while (zipInputStream.available() >= 1) {
        bytesRead += math.max(zipInputStream.read(buffer, bytesRead, 1 << 20), 0)
      }
      val ret = new Array[Byte](bytesRead)
      buffer.copyToArray(ret, 0, bytesRead)
      Some((name, ret))
    } else {
      None
    }
  }

  def close(): Unit = {
    zipInputStream.close()
  }
}

object SimpleZipFile {
  def withOutputStream(output: OutputStream)(f: SimpleZipOutputFile => Unit): Unit = {
    val zipFile = new SimpleZipOutputFile(output)
    try {
      f(zipFile)
    } finally {
      zipFile.close()
    }
  }

  def withInputStream(input: InputStream)(f: SimpleZipInputFile => Unit): Unit = {
    val zipFile = new SimpleZipInputFile(input)
    try {
      f(zipFile)
    } finally {
      zipFile.close()
    }
  }
}
