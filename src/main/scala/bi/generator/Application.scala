import java.util.{Calendar, Date, GregorianCalendar}

import scala.util.Random

package bi.generator {

import breeze.stats.distributions.Gaussian

case class FactEntry
(
  val factID: Int,
  val dateID: Int,
  val applicantID: Int,
  val applicationDateID: Int,
  val applicationID: Int,
  val applicationStatusID: Int,
  val vacancyID: Int) {
  override def toString() = {
    "FactID=%d | DateID=%d | ApplicantID=%d | ApplicationDateID==%d | applicationID=%d | ApplicationStatusID=%d | VacancyID=%d".format(
      factID, dateID, applicantID, applicationDateID, applicationID, applicationStatusID, vacancyID
    )
  }
}


case class FactEntryGenerator() {
  var lastFactID = 0

  def next(dateID: Int, application: Application, applicationStatus: ApplicationStatus): FactEntry = {
    lastFactID += 1
    FactEntry(lastFactID, dateID, application.applicantID, 0, application.applicationID, applicationStatus.applicationStatusID, application.vacancyID)
  }
}

case class GeneratorDate(
                          val date: Date)

case class Applicant(
                      val applicantID: Int,
                      val firstname: String,
                      val lastname: String,
                      val gender: String,
                      val age: Int) {
  override def toString() = {
    "ApplicantID=%d | FirstName=%s | LastName=%s | Gender=%s | Age=%d".format(
      applicantID, firstname, lastname, gender, age
    )
  }
}

case class ApplicantCollection(persons: Array[Array[String]]) {
  var applicantID: Int = 0

  def nextApplicantID(): Int = {
    val newApplicantID = applicantID + 1
    applicantID += 1
    return newApplicantID
  }

  val applicants = persons.map(p => {
    val age = Tools.EnsureRange(37 + (Gaussian(0, 1).draw() * 15).toInt, 18, 65);
    val applicant = Applicant(nextApplicantID(), p(0), p(1), p(2), age)
    if (applicant.applicantID < 10)
      println(">> " + applicant.toString())
    (applicant.applicantID, applicant)
  }).toMap
}


case class ApplicationStatus(val applicationStatusID: Int,
                             val applicationID: Int,
                             val status_ref: String,
                             val date: Date) {
  override def toString(): String = {
    return "ApplicationStatusID=%d | ApplicationID=%d | status_ref:%s | Date:%s".format(
      applicationStatusID, applicationID, status_ref, date.toString()
    )
  }
}

case class Application(val applicantID: Int,
                       val applicationID: Int,
                       val vacancyID: Int,
                       val applicationDate: Date,
                       val type_ref: String,
                       val quality_ref: String,
                       val recruiterID: Int,
                       val branchID: Int) {
  override def toString(): String = {
    return "ApplicantID=%d | ApplicationID=%d | VacancyID=%d | ApplicationDate=%d | type_ref=%s | quality_ref=%s | RecruiterID=%d | branchID=%d".format(
      applicantID, applicationID, vacancyID, 0, type_ref, quality_ref, recruiterID, branchID)
  }
}


case class DateRandomizer(beginningDate: Date, maxDaysDelta: Int) {
  var calendar = new GregorianCalendar()
  calendar.setTime(beginningDate)

  def next(): java.util.Date = {
    calendar.add(Calendar.HOUR, Random.nextInt(maxDaysDelta) * 24)
    return calendar.getTime
  }
}

case class StatusRandomizer() {
  var currentStatusIndex: Int = 0

  def allStates = List("Status1", "Status2", "Status3")

  def next(): String = {
    currentStatusIndex = (currentStatusIndex + 1) % 3
    allStates(currentStatusIndex)
  }
}

case class ApplicationGenerator(beginningDate: Date, vacancyID: Int, type_ref: String, quality_ref: String, recruiterID: Int, branchID: Int) {
  var applicationID: Int = 100
  var lastDate = new GregorianCalendar()

  lastDate.setTime(beginningDate)

  def nextApplicationID(): Int = {
    applicationID += 1
    return applicationID
  }

  def resetApplicationID: Int = {
    applicationID = 1
    return applicationID
  }

  def nextRandomDate(): Date = {
    lastDate.add(Calendar.HOUR, Random.nextInt(10) * 24)
    //println( lastDate.getTime().toString())

    return lastDate.getTime()
  }

  def generateNext(applicantID: Int): Application = {
    // ApplicationStatus(0,0,"", new GregorianCalendar())

    return Application(
      applicantID,
      nextApplicationID(),
      vacancyID,
      nextRandomDate(),
      type_ref,
      quality_ref,
      recruiterID,
      branchID)
  }
}

case class ApplicationStatusGenerator(maxDaysDelta: Int) {
  var beginningDate: Date = new Date(0, 0, 0)
  var applicationID: Int = 0
  var applicationStatusID = 0
  val dateRandomizer = DateRandomizer(beginningDate, maxDaysDelta)
  val statusRandomizer = StatusRandomizer()

  def setApplicationID(applicationID: Int) = {
    this.applicationID = applicationID
  }

  def generateNext(): ApplicationStatus = {
    applicationStatusID += 1
    return ApplicationStatus(applicationStatusID, applicationID, statusRandomizer.next(), dateRandomizer.next())
  }

  def relateTo(application: Application) = {
    applicationID = application.applicationID
    beginningDate = application.applicationDate
  }
}

}