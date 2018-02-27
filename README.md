# gradle-selenium-drivers-plugin
This is a gradle plugin to be able to install Selenium WebDrivers and browsers for testing.    
     
[![Build Status](https://travis-ci.org/zhurlik/gradle-selenium-drivers-plugin.svg?branch=master)](https://travis-ci.org/zhurlik/gradle-selenium-drivers-plugin)
[![Coverage Status](https://coveralls.io/repos/github/zhurlik/gradle-selenium-drivers-plugin/badge.svg?branch=master)](https://coveralls.io/repos/github/zhurlik/gradle-selenium-drivers-plugin)    

## Problem
Usually you need to setup a browser for corresponded WebDriver during developing your tests based on Selenium.
This plugin is going to help with installation. 

## Supported Operation Systems
1. Windows (requires [chocolatey.org](https://chocolatey.org/docs/installation) and Admin privileges)
2. Linux (unzipping)
3. Mac (not implemented yet)

## Browsers and WebDrivers
Here is a matrix to understand what we are using for downloading distributive    
* FireFox&Gecko    
  - Windows - [FireFox on chocolatey.org](https://chocolatey.org/packages/Firefox)
  - Linux - [FireFox Releases](https://ftp.mozilla.org/pub/firefox/releases/)
  - Mac - not yet
* Chrome
  - Windows - [GoogleChrome on chocolatey.org](https://chocolatey.org/packages/GoogleChrome)
  - open question
* PhantomJs
  - Linux and Mac OS X
    - [bitbucket](https://bitbucket.org/ariya/phantomjs/downloads/)
    - [Google Code](https://code.google.com/archive/p/phantomjs/downloads)
  - Windows - [PhantomJS on chocolatey.org](https://chocolatey.org/packages/PhantomJS)
* IE
  - Windows - [IE11 on chocolatey.org](https://chocolatey.org/packages/ie11)
* Edge
* Opera
  - Linux - ftp://ftp.opera.com/pub/opera/desktop/ based on https://forums.opera.com/topic/25213/direct-links-for-downloading-old-versions-tar-gz-files
  - Windows - [Opera on chocolatey.org](https://chocolatey.org/packages/opera)
* Safari
  - no extra actions (ensure that Allow Remote Automation is enabled)    

## How to use
TODO: make more examples

