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
    }
}
