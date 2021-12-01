===================================================================
== Spring MVC Controllers implemented in a dynamic language      ==
===================================================================

@author Rick Evans


1. OVERVIEW

This small application showcases implementing Spring MVC Controllers
using the dynamic language support introduced in Spring 2.0.

The web application is *very* simplistic, because the intent is
to convey the basics of the dynamic language support as applied to
Spring MVC and pretty much nothing else.

There is one Groovy file in the application. It is called
'FortuneController.groovy' and it is located in the 'war/WEB-INF/groovy'
folder. This Groovy script file is referenced by the 'fortune'
bean in the 'war/WEB-INF/fortune-servlet.xml' Spring MVC configuration file.

You will notice that the 'fortune' bean is set as refreshable via the use
of the 'refresh-check-delay' attribute on the <lang:groovy/> element. The
value of this attribute is set to '3000' which means that changes to the
'FortuneController.groovy' file will be picked up after a delay of 3 seconds.

If you deploy the application to Tomcat (for example), you can then go into
the exploded '/WEB-INF/groovy' folder and edit the 'FortuneController.groovy'
file directly. Any such changes that you make will be automatically picked up
and the 'fortune' bean will be reconfigured... all without having to stop,
redeploy and restart the application. Try it yourself... now admittedly
there is not a lot of complex logic in the 'FortuneController.groovy' file
(which is good because Controllers in Spring MVC should be as thin as possible).

You could try returning a default Fortune instead of delegating to the injected
FortuneService, or you could return a different logical view name, or
(if you are feeling more ambitious) you could try creating a custom
Groovy implementation of the FortuneService interface and try plugging that into
the web application. Perhaps your custom Groovy FortuneService could access
a web service to get some Fortunes, or apply some different randomizing logic
to the returned Fortune, or whatever. The key point is that you will be able to make
these changes without having to redeploy (or bounce) your application. This is
a great boon with regard to rapid prototyping.


2. BUILD AND DEPLOYMENT

This directory contains the web app source.
For deployment, it needs to be built with Apache Ant.
The only requirements are JDK >=1.4 (it is Groovy that requires at a
minimum JDK 1.4) and Ant >=1.5.

Run "build.bat" in this directory for available targets (e.g. "build.bat build",
"build.bat warfile"). Note that to start Ant this way, you'll need an XML parser
in your classpath (e.g. in "%JAVA_HOME%/jre/lib/ext"; included in JDK 1.4).
You can use "warfile.bat" as a shortcut for WAR file creation.
The WAR file will be created in the "dist" directory.
