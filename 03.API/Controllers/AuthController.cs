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

        // ĐĂNG KÝ (Register)
        [HttpPost("register")]
        public IActionResult Register([FromBody] RegisterRequest req)
        {
            var emailExists = _context.Users.Any(u => u.Email == req.Email);
            if (emailExists)
                return Conflict(new { success = false, message = "Email đã tồn tại" });

            // Có thể bỏ kiểm tra username, vì username sẽ = email
            var user = new User
            {
                Email = req.Email,
                Username = req.Email,
                Password = req.Password
            };

            _context.Users.Add(user);
            _context.SaveChanges();
            return Ok(new { success = true, message = "Đăng ký thành công" });
        }

        // ĐĂNG NHẬP (Login)
        [HttpPost("login")]
        public IActionResult Login([FromBody] LoginRequest req)
        {
            var matched = _context.Users
                .FirstOrDefault(u => u.Username == req.Username && u.Password == req.Password);

            if (matched == null)
                return Unauthorized("Tài khoản hoặc mật khẩu không đúng!");

            // Có thể chỉ trả về một số trường (userId, email...), không nên trả về password
            return Ok(new
            {
                message = "Đăng nhập thành công",
                user = new
                {
                    matched.Id,
                    matched.Username,
                    matched.Email,
                    matched.FullName,
                    matched.AvatarUrl,
                    matched.Phone,
                    matched.Birthdate,
                    matched.Address
                }
            });
        }
    }
}
