namespace API.CongViec.Models
{
    public class TaskModel
    {
        public int Id { get; set; }
        public string Title { get; set; }
        public string? Description { get; set; }
        public DateTime? DueDate { get; set; }
        public int? Priority { get; set; }
        public bool Completed { get; set; }
        public int PlanId { get; set; }
    }
}
