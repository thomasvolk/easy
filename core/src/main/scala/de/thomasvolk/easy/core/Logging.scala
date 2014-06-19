package de.thomasvolk.easy.core

import org.slf4j.LoggerFactory

trait Logging {
  val logger = LoggerFactory.getLogger(this.getClass)
  
  def debug(text: String): Unit = {
    logger.debug(text)
  }
  
  def info(text: String): Unit = {
    logger.info(text)
  }
  
  def error(text: String): Unit = {
    logger.error(text)
  }
  
  def error(text: String, t: Throwable): Unit = {
    logger.error(text, t)
  }
  
  def warn(text: String): Unit = {
    logger.warn(text)
  }

  def warn(text: String, t: Throwable): Unit = {
    logger.warn(text, t)
  }
}