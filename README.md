# raven-java

Raven is the Java SDK for [Sentry](https://sentry.io/). It provides out-of-the-box support for
many popular JVM based frameworks and libraries, including Android, Log4j, Logback, and more.

In most cases using one of the existing integrations is preferred, but Raven additionally provides
a low level client for manually building and sending events to Sentry that can be used in any JVM
based application.

## About the fork

As raven-java doesn't support Java 6 and I had to try it on a project that uses Java 6, I remade one of the modules
according to Java 6 standards. I also removed other modules that I don't need.

## Resources

* [Documentation](https://docs.sentry.io/clients/java/)
* [Bug Tracker](http://github.com/getsentry/raven-java/issues)
* [Code](http://github.com/getsentry/raven-java)
* [Mailing List](https://groups.google.com/group/getsentry)
* [IRC](irc://irc.freenode.net/sentry)  (irc.freenode.net, #sentry)
* [Travis CI](http://travis-ci.org/getsentry/raven-java)

