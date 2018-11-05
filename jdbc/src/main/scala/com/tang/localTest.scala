package com.tang

object localTest extends QueryBuilder {
  def main(args: Array[String]): Unit = {
    val queryString = "select * from events order by id desc limit 10"
    val res = query(queryString)

    val insert =
      """insert into
        |	events (app_id, sfid , tid ,open_id, session_id, dwell_time, event_type, subtype, parameter, extra_parameter, track_time, namespace, repeat_times,key)
        |	values (2130, '0','0','o9U9Z5JRTbeyrTEjpawcMzsB68DM', 66908572, 0,
        |	'pv', 'pages/showcase/index/index', 0,
        |	'{"enterAppId":"2130","key":"enterApp"}', '2018-10-31 21:00:29.971',
        |	'uat2', 9,'pages/showcase/index/index')
      """.stripMargin
    val id = insertAndGetKey(insert)
    println(id)
  }

}
