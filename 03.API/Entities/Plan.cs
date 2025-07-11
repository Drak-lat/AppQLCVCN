

namespace API.CongViec.Entities
{
    public class Plan
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string? Description { get; set; }
        public DateOnly? StartDate { get; set; } 
        public DateOnly? EndDate { get; set; }   
        public int UserId { get; set; }
        public User User { get; set; }
        public ICollection<TaskModel> Tasks { get; set; }
    }
}
