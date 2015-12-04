package bi.dao

import bi.models.Tables
import io.strongtyped.active.slick.Lens._
import io.strongtyped.active.slick._
import slick.ast.BaseTypedType

import scala.language.postfixOps

/**
 * For more on ActiveSlick see: http://www.strongtyped.io/active-slick/
 * Created by ttruong on 03.12.15.
 */
object DimJobPlatformDao extends EntityActions with PostgresProfileProvider {

  //import Slick traits and classes like Rep and DBIO
  import jdbcProfile.api._

  val baseTypedType = implicitly[BaseTypedType[Id]]

  /**
   * NOTE: There is no primary key! In this case we use the person ID
   */
  type Id = Long;
  type Entity = Tables.DimPoolRow
  type EntityTable = Tables.DimPool
  val tableQuery = Tables.DimPool

  def $id(table: EntityTable): Rep[Id] = table.personId

  //getter/setter for id
  val idLens = lens { entity: Entity => Option(entity.personId)} //getter function
                    { (entity, personId) => entity.copy(personId = personId.get)} //setter function

}
