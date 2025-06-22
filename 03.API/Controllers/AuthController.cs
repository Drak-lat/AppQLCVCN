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
            var existing = _context.Users.FirstOrDefault(u => u.Username == user.Username);
            if (existing != null)
                return BadRequest("Username already exists");

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

            return Ok(new { Message = "Đăng nhập thành công", UserId = matched.Id });
        }
    }
}
