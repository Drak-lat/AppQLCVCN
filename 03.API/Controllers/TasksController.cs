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

        // Lấy tất cả Task theo PlanId
        [HttpGet("plan/{planId}")]
        public IActionResult GetByPlan(int planId)
        {
            var tasks = _context.Tasks
                .Where(t => t.PlanId == planId)
                .ToList();
            return Ok(tasks);
        }

        // Lọc Task hoàn thành theo PlanId
        [HttpGet("plan/{planId}/completed")]
        public IActionResult GetByCompletion(int planId, [FromQuery] bool status)
        {
            var tasks = _context.Tasks
                .Where(t => t.PlanId == planId && t.Completed == status)
                .ToList();
            return Ok(tasks);
        }

        // Lọc theo Priority theo PlanId
        [HttpGet("plan/{planId}/priority")]
        public IActionResult GetByPriority(int planId, [FromQuery] int level)
        {
            var tasks = _context.Tasks
                .Where(t => t.PlanId == planId && t.Priority == level)
                .ToList();
            return Ok(tasks);
        }

        // Search Task theo keyword theo PlanId
        [HttpGet("plan/{planId}/search")]
        public IActionResult SearchTasks(int planId, [FromQuery] string keyword)
        {
            var tasks = _context.Tasks
                .Where(t => t.PlanId == planId &&
                            (t.Title.Contains(keyword) || t.Description.Contains(keyword)))
                .ToList();
            return Ok(tasks);
        }

        // Tạo Task mới
        [HttpPost]
        public IActionResult Create([FromBody] TaskModel task)
        {
            _context.Tasks.Add(task);
            _context.SaveChanges();
            return Ok(task);
        }

        // Cập nhật Task
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
            task.PlanId = updatedTask.PlanId;

            _context.SaveChanges();
            return Ok(task);
        }

        // Xóa Task
        [HttpDelete("{id}")]
        public IActionResult Delete(int id)
        {
            var task = _context.Tasks.FirstOrDefault(t => t.Id == id);
            if (task == null) return NotFound();

            _context.Tasks.Remove(task);
            _context.SaveChanges();
            return Ok("Deleted");
        }
    }
}
