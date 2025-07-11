
using System.Text.Json.Serialization;

namespace API.CongViec.Models
{
    public class Plan
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string? Description { get; set; }
        public DateOnly? StartDate { get; set; }
        public DateOnly? EndDate { get; set; }
        public int? UserId { get; set; }


        [JsonIgnore]
        public ICollection<TaskModel> Tasks { get; set; } = new List<TaskModel>();
    }
}
