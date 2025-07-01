using Microsoft.AspNetCore.Mvc;
using API.CongViec.Data;
using API.CongViec.Models;

namespace API.CongViec.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class AuthController : ControllerBase
    {
        private readonly AppDbContext _context;

        public AuthController(AppDbContext context)
        {
            _context = context;
        }

        [HttpPost("Register")]
        public IActionResult Register([FromBody] User user)
        {
            var emailExists = _context.Users.Any(u => u.Email == user.Email);
            if (emailExists)
                return Conflict(new { success = false, message = "Email đã tồn tại" });

            // Kiểm tra username đã tồn tại (nếu cần)
            var usernameExists = _context.Users.Any(u => u.Username == user.Username);
            if (usernameExists)
                return Conflict(new { success = false, message = "Username đã tồn tại" });

            user.Username = user.Email;

            _context.Users.Add(user);
            _context.SaveChanges();
            return Ok(new { success = true, message = "Đăng ký thành công" });
        }

        [HttpPost("login")]
        public IActionResult Login([FromBody] User user)
        {
            var matched = _context.Users
                .FirstOrDefault(u => u.Username == user.Username && u.Password == user.Password);

            if (matched == null)
                return Unauthorized("Invalid credentials");

            // Trả về user thực tế từ DB, KHÔNG phải object 'user' nhận từ client!
            return Ok(new { Message = "Đăng nhập thành công", user = matched });
        }

    }
}
