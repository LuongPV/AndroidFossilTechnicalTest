package com.pv.trackme.location

typealias NoDataListener = () -> Unit

typealias DataListener<DataType> = (data: DataType) -> Unit

typealias TwoDataListener<DataType1, DataType2> = (data1: DataType1, data2: DataType2) -> Unit