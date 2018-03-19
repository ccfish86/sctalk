REM proto 3.4.0
call protoc  ./*.proto --js_out=import_style=commonjs,binary:./js
pause