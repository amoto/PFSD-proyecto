package domain.queries

trait Query[T] {

  def execute(): Either[Throwable, T]

}
