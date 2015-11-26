package bi.dao

import java.util.concurrent.Executors

import bi.models.Tables
import bi.models.Tables._
import bi.models.Tables.profile.api._

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by ttruong on 26.11.15.
 */
trait FactApplicantDao {

  def findById(id: Long): Future[Option[Tables.FactApplicantRow]]
  def findByApplicantId(id: Long): Future[Option[Tables.FactApplicantRow]]
  def insert(dimApplicantId: Long):Future[Tables.FactApplicantRow]
}

case class FactApplicantDaoImpl(db: Database) extends FactApplicantDao {
  private implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(5))

  def findById(id: Long): Future[Option[Tables.FactApplicantRow]] = {
    val query: Query[Tables.FactApplicant, Tables.FactApplicant#TableElementType, Seq] = FactApplicant.filter(_.factId === id)
    val action = query.result.headOption
    db.run(action)

  }

  def findByApplicantId(id: Long): Future[Option[Tables.FactApplicantRow]] = {
    val query: Query[Tables.FactApplicant, Tables.FactApplicant#TableElementType, Seq] = FactApplicant.filter(_.dimApplicantId === id)
    val action = query.result.headOption
    db.run(action)

  }

  def insert(dimApplicantId: Long):Future[Tables.FactApplicantRow] = {
    val insertQuery = FactApplicant returning FactApplicant.map(_.factId) into (
      (item, id) => item.copy(factId = id))
    val action = insertQuery += FactApplicantRow(-1, dimApplicantId, 1, 1, 1, 1, 1, 1, 1, 1, 1, Some(1), Some(1), Some(1), Some(1), Some(1), Some(1), 1)
    db.run(action)
  }
}