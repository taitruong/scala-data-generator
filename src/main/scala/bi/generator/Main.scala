package bi.generator


import java.io.File
import java.text.SimpleDateFormat
import java.util.GregorianCalendar

import scala.collection.mutable

object Main extends App {

  def dateFormatter = new SimpleDateFormat("yyyy MM dd HH:mm:ss")
  def beginningDate = new GregorianCalendar(2013,0,1)


  def qualityfunc : String = {
    return "AA"
  }

  override def main(args:Array[String]): Unit = {
    // create Vacancies

    val vacancies = List(
      Vacancy(1, "Java Developer"),
      Vacancy(2, "Scrum Master"),
      Vacancy(3, "Product Owner"))

    val hardFacts = HardFacts()

    val applicantCollection = ApplicantCollection(hardFacts.persons)
    val applicationsGenerator = ApplicationGenerator(beginningDate.getTime(), 1, "Normal", qualityfunc, 0, 0)
    val applicationStatusGenerator = ApplicationStatusGenerator(10)
    val factEntryGenerator = FactEntryGenerator()

    val allApplications = applicantCollection.applicants.slice(0, 10).map { applicant =>
      {
        val newApplication = applicationsGenerator.generateNext(applicant._2.applicantID)
         println( newApplication.toString() )
        (newApplication.applicationID, newApplication)
      } }.toMap

    var allFacts = mutable.LinkedHashMap[Int, FactEntry]()
    var allStatuses = mutable.LinkedHashMap[Int, ApplicationStatus]()

    allApplications.foreach(application => {
      applicationStatusGenerator.relateTo(application._2)
      val newStatuses = (1 to 5) map (_ => applicationStatusGenerator.generateNext())

      val newFactList = newStatuses.map { status => factEntryGenerator.next(0, application._2, status) }
      allFacts = allFacts ++ newFactList.map { fact => (fact.factID, fact) }
      allStatuses = allStatuses ++ newStatuses.map { status => (status.applicationStatusID, status )}
      println(application.toString())
    })


    mutable.LinkedHashMap(allFacts.toSeq.sortWith(_._1 < _._1): _*).foreach(entry => {
      println("FactEntry - " + entry.toString() )
      println("  Status - " + allStatuses(entry._2.applicationStatusID).toString())
      println("  Application - "  + allApplications(entry._2.applicationID).toString() )
      println("  Applicant - " + applicantCollection.applicants(entry._2.applicantID).toString())
      //      println( " Application = " + applications.collectFirst {
      //      case e if( e.applicationID == entry._2.applicationID ) => e
    })
  }
}





/*
var lastDate = beginningDate

def nextRandomDate( gc: GregorianCalendar) : GregorianCalendar =
{ gc.add(Calendar.HOUR, Random.nextInt(10)*24)
  return gc
}

val statusDates = List.fill(10)(beginningDate)

statusDates.view.zipWithIndex foreach {
  e => lastDate = nextRandomDate(lastDate)
    e._1.setTime( lastDate.getTime() )
}

for( x <- 0 to statusDates.length - 1 ) {
  println( x, dateFormatter.format( statusDates(x).getTime() ) )
}
*/

