using Microsoft.AspNetCore.Mvc;
using API.CongViec.Data;
using API.CongViec.Models;
using Microsoft.EntityFrameworkCore;
using API.CongViec.Model;

namespace API.CongViec.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class TasksController : ControllerBase
    {
        private readonly AppDbContext _context;

        public TasksController(AppDbContext context)
        {
            _context = context;
        }

        // Lấy toàn bộ công việc theo UserId
        [HttpGet("user/{userId}")]
        public IActionResult GetByUser(int userId)
        {
            var tasks = _context.Tasks
                .Where(t => t.UserId == userId)
                .ToList();

            return Ok(tasks);
        }

        // Tạo công việc mới
        [HttpPost]
        public IActionResult Create([FromBody] TaskModel task)
        {
            _context.Tasks.Add(task);
            _context.SaveChanges();
            return Ok(task);
        }

        // Cập nhật công việc theo Id
        [HttpPut("{id}")]
        public IActionResult Update(int id, [FromBody] TaskModel updatedTask)
        {
            var task = _context.Tasks.FirstOrDefault(t => t.Id == id);
            if (task == null) return NotFound();

            task.Title = updatedTask.Title;
            task.Description = updatedTask.Description;
            task.DueDate = updatedTask.DueDate;
            task.Priority = updatedTask.Priority;
            task.Completed = updatedTask.Completed;

            _context.SaveChanges();
            return Ok(task);
        }

        // Xóa công việc
        [HttpDelete("{id}")]
        public IActionResult Delete(int id)
        {
            var task = _context.Tasks.FirstOrDefault(t => t.Id == id);
            if (task == null) return NotFound();

            _context.Tasks.Remove(task);
            _context.SaveChanges();
            return Ok("Deleted");
        }
        // GET: api/tasks/user/1/completed?status=true
        [HttpGet("user/{userId}/completed")]
        public IActionResult GetByCompletion(int userId, [FromQuery] bool status)
        {
            var tasks = _context.Tasks
                .Where(t => t.UserId == userId && t.Completed == status)
                .ToList();

            return Ok(tasks);
        }
        // GET: api/tasks/user/1/priority?level=2
        [HttpGet("user/{userId}/priority")]
        public IActionResult GetByPriority(int userId, [FromQuery] int level)
        {
            var tasks = _context.Tasks
                .Where(t => t.UserId == userId && t.Priority == level)
                .ToList();

            return Ok(tasks);
        }
        // GET: api/tasks/user/1/search?keyword=báo
        [HttpGet("user/{userId}/search")]
        public IActionResult SearchTasks(int userId, [FromQuery] string keyword)
        {
            var tasks = _context.Tasks
                .Where(t => t.UserId == userId &&
                            (t.Title.Contains(keyword) || t.Description.Contains(keyword)))
                .ToList();

            return Ok(tasks);
        }

    }
}
