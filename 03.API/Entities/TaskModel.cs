using API.CongViec.Models;

namespace API.CongViec.Entities
{
    public class TaskModel
    {
    public int Id { get; set; }
    public string Title { get; set; }
    public string? Description { get; set; }
    public DateOnly? DueDate { get; set; }
    public int? Priority { get; set; }
    public bool Completed { get; set; }
    public int PlanId { get; set; }
    public Plan Plan { get; set; }
    }
}
