package bi.dao

import bi.models.Tables
import io.strongtyped.active.slick._
import slick.ast.BaseTypedType
import io.strongtyped.active.slick.Lens._
import scala.language.postfixOps

/**
 * For more on ActiveSlick see: http://www.strongtyped.io/active-slick/
 * Created by ttruong on 03.12.15.
 */
object FactApplicantDao extends EntityActions with PostgresProfileProvider {

  //import Slick traits and classes like Rep and DBIO
  import jdbcProfile.api._

  val baseTypedType = implicitly[BaseTypedType[Id]]

  type Id = Long;
  type Entity = Tables.FactApplicantRow
  type EntityTable = Tables.FactApplicant
  val tableQuery = Tables.FactApplicant

  def $id(table: EntityTable): Rep[Id] = table.factId

  //getter/setter for id
  val idLens = lens { entity: Entity => Option(entity.factId )} //getter function
                    { (entity, factId) => entity.copy(factId = factId.get)} //setter function

  def findByApplicantId(applicantId:Long): DBIO[Seq[Entity]] = {
    tableQuery.filter(_.dimApplicantId === applicantId).result
  }

}
