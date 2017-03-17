package scodec

package object meta {
  implicit class Pluralizer(val num: Long) extends AnyVal {
    def plural(what: String): String = {
      val s = if (num > 1) "s" else ""
      s"$num $what$s"
    }

    def bitsOrBytes: String = {
      num.plural("bit") + {
        if (num % 8 == 0) s" (${(num / 8).plural("byte")})"
        else ""
      }
    }
  }
}
