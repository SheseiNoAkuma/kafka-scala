addSbtPlugin("org.jetbrains" % "sbt-ide-settings" % "1.1.0")
addSbtPlugin("com.julianpeeters" % "sbt-avrohugger" % "2.0.0-RC22")

dependencyOverrides += "com.julianpeeters" %% "avrohugger-core" % "1.0.0-RC21"
