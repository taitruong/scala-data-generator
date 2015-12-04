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
object DimVacancyDao extends EntityActions with PostgresProfileProvider {

  //import Slick traits and classes like Rep and DBIO
  import jdbcProfile.api._

  val baseTypedType = implicitly[BaseTypedType[Id]]

  type Id = Long;
  type Entity = Tables.DimVacancyRow
  type EntityTable = Tables.DimVacancy
  val tableQuery = Tables.DimVacancy

  def $id(table: EntityTable): Rep[Id] = table.dimId

  //getter/setter for id
  val idLens = lens { entity: Entity => Option(entity.dimId )} //getter function
                    { (entity, dimId) => entity.copy(dimId = dimId.get)} //setter function

}
