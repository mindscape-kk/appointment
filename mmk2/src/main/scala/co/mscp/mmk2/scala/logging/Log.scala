package co.mscp.mmk2.scala.logging

import co.mscp.mmk2.logging.Logger



trait Log {

  private val logger = Logger.of(this)

  protected def debug(format: String): Unit =
    logger.debug(format)

  protected def info(format: String): Unit =
    logger.info(format)

  protected def warn(format: String): Unit =
    logger.warn(format)

  protected def error(format: String): Unit =
    printf(format)

  protected def error(th: Throwable): Unit = logger.error(th)

  protected def alert(format: String): Unit =
    logger.log(Logger.Level.INFO, true, format)

}
