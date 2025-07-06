using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using API.CongViec.Data;
using API.CongViec.Models;

namespace API.CongViec.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class PlansController : ControllerBase
    {
        private readonly AppDbContext _context;

        public PlansController(AppDbContext context)
        {
            _context = context;
        }

        // GET: api/plans/user/1
        [HttpGet("user/{userId}")]
        public IActionResult GetByUser(int userId)
        {
            var plans = _context.Plans
                .Where(p => p.UserId == userId)
                .ToList();
            return Ok(plans);
        }

        // GET: api/plans/5
        [HttpGet("{id}")]
        public IActionResult GetById(int id)
        {
            var plan = _context.Plans
                .Include(p => p.Tasks)
                .FirstOrDefault(p => p.Id == id);
            if (plan == null) return NotFound();
            return Ok(plan);
        }

        // POST: api/plans
        [HttpPost]
        public IActionResult Create([FromBody] Plan model)
        {
            if (model == null || model.UserId <= 0 || string.IsNullOrWhiteSpace(model.Name))
                return BadRequest("Dữ liệu kế hoạch không hợp lệ!");

            _context.Plans.Add(model);
            _context.SaveChanges();
            return CreatedAtAction(nameof(GetById), new { id = model.Id }, model);
        }

        // PUT: api/plans/5
        [HttpPut("{id}")]
        public IActionResult Update(int id, [FromBody] Plan model)
        {
            var plan = _context.Plans.FirstOrDefault(p => p.Id == id);
            if (plan == null) return NotFound();

            plan.Name = model.Name;
            plan.Description = model.Description;
            plan.StartDate = model.StartDate;
            plan.EndDate = model.EndDate;
            // Không update UserId (nếu muốn thì mở comment dưới)
            // plan.UserId = model.UserId;

            _context.SaveChanges();
            return Ok(plan);
        }

        // DELETE: api/plans/5
        [HttpDelete("{id}")]
        public IActionResult Delete(int id)
        {
            var plan = _context.Plans
                .Include(p => p.Tasks)
                .FirstOrDefault(p => p.Id == id);

            if (plan == null) return NotFound();

            // Nếu muốn xóa luôn các task liên quan (nên kiểm tra/cảnh báo trước khi xóa)
            if (plan.Tasks != null && plan.Tasks.Any())
                _context.Tasks.RemoveRange(plan.Tasks);

            _context.Plans.Remove(plan);
            _context.SaveChanges();
            return Ok("Deleted");
        }
    }
}
