using API.CongViec.Model;

namespace API.CongViec.Models
{
    public class Plan
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string? Description { get; set; }
        public DateTime? StartDate { get; set; }
        public DateTime? EndDate { get; set; }
        public int UserId { get; set; }
        public User User { get; set; }
        public ICollection<TaskModel> Tasks { get; set; }
    }
}
