package bi.generator

/**
 * Created by ttruong on 04.12.15.
 */
object DimGeographyGenerator extends App {

  println(">>>Generating geography")
  private val file = getClass.getResourceAsStream("/rawdata/plz-ort.txt")
  val data = try {
    val iter = scala.io.Source.fromInputStream(file).getLines()
    iter.map(line => {
      val elements = line.split(Array(' '))
      if (elements.length != 2) {
        throw new RuntimeException(line + " contains an error (more or less than 2 elements)")
      }
      elements
    }).toList
  } finally {
    file.close()
  }

  println("First row: " + data(0)(0) + " - " + data(0)(1))



}
