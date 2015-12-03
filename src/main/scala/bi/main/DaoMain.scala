package bi.main

import java.util.concurrent.Executors

import bi.models.Tables
import bi.models.Tables.profile.api._
import bi.dao._
import io.strongtyped.active.slick.ActiveRecord

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by ttruong on 26.11.15.
 */
object DaoMain extends App {

  println(">>>starting BI data generator")
  import scala.concurrent.ExecutionContext.Implicits.global
  val db = Database.forConfig("db")

  try {

    val id = 10L

    val future:Future[Option[Tables.FactApplicantRow]] = db.run(FactApplicantDao.findOptionById(id))
    //val future = factApplicantDao.findById(id)

    Thread.sleep(1000)

    future.onSuccess {
      case Some(applicant) => println(">>> Applicant found: " + applicant)
      case None => println(">>> Applicant with id not found:" + id)
    }

    future.onFailure {
      case error =>
        error.printStackTrace
        println(">>> Error: " + error)
    }

    val dimApplicantId = 123L

    val newEntity = Tables.FactApplicantRow(-1, dimApplicantId, 1, 1, 1, 1, 1, 1, 1, 1, 1, Some(1), Some(1), Some(1), Some(1), Some(1), Some(1), 1)
    val insertAction = FactApplicantDao.insert(newEntity)

    val insertFuture = db.run(insertAction)

    Thread.sleep(1000)
    insertFuture.onSuccess{
      case id =>
        println(">>> Created with fact id " + id)
    }

    insertFuture.onFailure{
      case error =>
        error.printStackTrace
        println(">>> Error: " + error)
    }

    val countFuture = db.run(FactApplicantDao.count)

    Thread.sleep(1000)
    countFuture.onSuccess{
      case count =>
        println(">>> Total size: " + count)
    }

    countFuture.onFailure{
      case error =>
        error.printStackTrace
        println(">>> Error: " + error)
    }

  } finally db.close()

}
