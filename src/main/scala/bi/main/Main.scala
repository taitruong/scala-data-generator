package bi.main

import java.util.concurrent.Executors

import bi.dao.FactApplicantDaoImpl
import bi.models.Tables
import bi.models.Tables._
import bi.models.Tables.profile.api._

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by ttruong on 26.11.15.
 */
object Main extends App {

  println(">>>starting BI data generator")
  private implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(5))
  val db = Database.forConfig("db")

  try {

    val factApplicantDao = FactApplicantDaoImpl(db)

    val id = 10L
    val future = factApplicantDao.findById(id)

    Thread.sleep(1000)

    future.onSuccess {
      case Some(applicant) => println(">>> applicant: " + applicant)
      case None => println(">>> applicant with id not found:" + id)
    }

    future.onFailure {
      case error =>
        error.printStackTrace
        println(">>> error: " + error)
    }

    val dimApplicantId = 123L
    val insertFuture = factApplicantDao.insert(dimApplicantId)
    Thread.sleep(1000)
    insertFuture.onSuccess{
      case applicant =>
        println(">>>> created with fact id " + applicant.factId + " and dim application id " + applicant.dimApplicantId)
    }

    insertFuture.onFailure{
      case error =>
        error.printStackTrace
        println(">>> error: " + error)
    }


  } finally db.close()

}
