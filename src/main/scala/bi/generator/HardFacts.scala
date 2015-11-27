package bi.generator

/**
  * Created by YSE on 26.11.2015.
  */

import scala.io.Source

case class HardFacts() {

  private val personFile = Source.fromFile("src/main/resources/rawdata/PersonData.csv")
  val iter = personFile.getLines()
  val persons = iter.map(line => {
    val elements = line.split(Array(';', ' '))
    if( elements.length != 3)
      println( line + " contains an error (more or less than 3 elements)")
    elements
  }).toArray
  personFile.close()
}