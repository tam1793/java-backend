# java-backend
## Project được chia thành 3 phần lớn
* main
  * chứa hàm main để run server 
* core
  * chứa các class defined HttpServlet, ApiOutput,
* app
  * chứa các class xử lý request
  
Demo:
  1. Chạy file DemoJavaServer.java để run server. Server sẽ chạy trên localhost port 9090
  2. Chạy browser với link http://localhost:9090/demoService?action=plus&number1=1&number2=2 (thực hiện lệnh get với các parameter action,number1,number2 xem file DemoApiController.java và DemoService.java)
  3. Thay action=mult để get api của phép nhân.
