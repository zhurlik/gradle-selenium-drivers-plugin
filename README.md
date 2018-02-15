# gradle-selenium-drivers-plugin
This is a gradle plugin to be able to install Selenium WebDrivers and browsers for testing.    
     
[![Build Status](https://travis-ci.org/zhurlik/gradle-selenium-drivers-plugin.svg?branch=master)](https://travis-ci.org/zhurlik/gradle-selenium-drivers-plugin)
[![Coverage Status](https://coveralls.io/repos/github/zhurlik/gradle-selenium-drivers-plugin/badge.svg?branch=master)](https://coveralls.io/repos/github/zhurlik/gradle-selenium-drivers-plugin)    

## Problem
Usually you need setup a browser for corresponded WebDriver during developing your tests based on Selenium.
This plugin is going to help with installation. 

## Opration Systems
We are planning to support the following OS:
1. Windows (requires [chocolatey.org](https://chocolatey.org/docs/installation) and Admin privileges)
2. Linux (unzipping)
3. Mac (not implemented yet)

## Browsers
Here is a matrix to understand what we are using for downloading distributive    
* FireFox    
  - Windows - choco
  - Linux - [FireFox Releases](https://ftp.mozilla.org/pub/firefox/releases/)
  - Mac - not yet
* Chrome
  - Windows - choco
  - open question
* PhantomJs
  - [bitbucket](https://bitbucket.org/ariya/phantomjs/downloads/)
  - [github](https://github.com/ariya/phantomjs/releases/)
  - [Google Code](https://code.google.com/archive/p/phantomjs/downloads)
* IE
* Edge
* Safari    

## Selenium-WebDriver’s Drivers

## How to use
TODO: make more samples

