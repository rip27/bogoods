<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('/user', function () {
    return view('welcome');
});
Route::get('/login', function () {
    return view('login');
});
Route::get('/index', function () {
    return view('index');
});
Route::get('/admin', function () {
    return view('admin/admin');
});
Route::get('/admin/datauser', function () {
    return view('admin/datauser');
});
Route::get('/admin/datastore', function () {
    return view('admin/datastore');
});
