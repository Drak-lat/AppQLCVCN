using Microsoft.AspNetCore.Mvc;
using API.CongViec.Data;
using API.CongViec.Models;

namespace API.CongViec.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class UsersController : ControllerBase
    {
        private readonly AppDbContext _context;

        public UsersController(AppDbContext context)
        {
            _context = context;
        }

        // GET: api/Users/5
        [HttpGet("{id}")]
        public IActionResult GetProfile(int id)
        {
            var user = _context.Users.Find(id);
            if (user == null)
                return NotFound();
            return Ok(user);
        }

        // PUT: api/Users/5
        [HttpPut("{id}")]
        public IActionResult UpdateProfile(int id, [FromBody] User userUpdate)
        {
            var user = _context.Users.Find(id);
            if (user == null)
                return NotFound();

            user.FullName = userUpdate.FullName;
            user.Email = userUpdate.Email;
            user.Phone = userUpdate.Phone;
            user.Birthdate = userUpdate.Birthdate;
            user.Address = userUpdate.Address;
            user.AvatarUrl = userUpdate.AvatarUrl;

            _context.SaveChanges();

            return Ok(user);
        }

        [HttpPost("upload-avatar/{id}")]
        public async Task<IActionResult> UploadAvatar(int id, IFormFile file)
        {
            var user = await _context.Users.FindAsync(id);
            if (user == null) return NotFound("User not found");
            if (file == null || file.Length == 0)
                return BadRequest("File không hợp lệ");

            // Tạo thư mục images nếu chưa có
            var uploadFolder = Path.Combine(Directory.GetCurrentDirectory(), "wwwroot/images");
            if (!Directory.Exists(uploadFolder)) Directory.CreateDirectory(uploadFolder);

            // Tạo tên file duy nhất
            var fileName = $"avatar_{id}_{DateTime.Now.Ticks}{Path.GetExtension(file.FileName)}";
            var filePath = Path.Combine(uploadFolder, fileName);

            // Lưu file lên server
            using (var stream = new FileStream(filePath, FileMode.Create))
            {
                await file.CopyToAsync(stream);
            }

            // Tạo URL public
            var request = HttpContext.Request;
            var baseUrl = $"{request.Scheme}://{request.Host}";
            var avatarUrl = $"{baseUrl}/images/{fileName}";

            // Cập nhật vào DB
            user.AvatarUrl = avatarUrl;
            await _context.SaveChangesAsync();

            return Ok(new { success = true, avatarUrl });
        }

    }
}
