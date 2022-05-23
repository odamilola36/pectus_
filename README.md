# Pectus assessment

Pectus finance assessment

A simple rest api to get expanse data from a file (expanse.csv) and perform basic sort and filter operations on it

# Requirements
To run this application, you'll need docker installed.
clone the [repository](https://github.com/odamilola36/pectus_)
* cd into pectus_
* run ```chmod +x run.sh && ./run.sh``` on the terminal(with docker installed)
* alternatively, within the pectus_ directory run ```chmod +x runlocal.sh && ./runlocal.sh```
* the application runs on port ```8082```
* swagger url is [swagger link](http://localhost:8082/swagger-ui/index.html)


## API Reference

All URIs are relative to *http://localhost:8080*


### Expanse Api

Method | HTTP request | Description
------------- | ------------- | -------------
[**expanses**]| **GET** /api/expanse_data/expanses | get all expanse data

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **pageSize** | **Long**| number of elements per page |[optional defaults to 10]
 **sort** | **List**| sort parameter which can be department, projectName, memberName, date, amount in this format ```department=asc,...``` | [optional]
 **filter** | **List**| filter parameter which can be department, projectName, memberName, date, amount in this format ```key>=<value,...``` | [optional]
 **pageNumber** | **Long**| page number | [optional defaults to 0]

No authorization required

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAggregates**]() | **Get** /api/expanse_data/aggregates?by=[department, projectName, memberName, date](not optional) | group all expanses

Method | HTTP request | Description
------------- | ------------- | -------------
[**getOneExpanse**]() | **GET** /api/expanse_data/expanse/{id} | Get expanse with the given id

### Parameters
This takes an optional list of fields that should be returned, if omitted, all fields are returned.
## Demo

Here is a [link](https://www.loom.com/share/ae31e431f7804f928669f90bb276ace4) to the api demo.

